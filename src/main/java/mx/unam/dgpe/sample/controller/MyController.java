package mx.unam.dgpe.sample.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class MyController extends AbstractVerticle {

    private static String pba="";
    private static final Logger logger = Logger.getLogger(MyController.class);
    
    public void start(Future<Void> fut) {
        logger.info("Inicializando Vertical");
        Router router = Router.router(vertx);
        //router.route("/*").handler(StaticHandler.create("assets")); // para invocar asi: http://localhost:8080/index.html
        // el directorio "upload-folder" será creado en la misma ubicación que el jar fue ejecutado
        router.route().handler(BodyHandler.create().setUploadsDirectory("upload-folder"));
        router.get("/api/primero").handler(this::primero);
        router.post("/api/segundo").handler(this::segundo);
		router.get("/api/suma").handler(this::suma);      
        router.get("/api/resta").handler(this::resta);
        router.get("/api/multiplica").handler(this::multiplica);
        router.get("/api/divide").handler(this::divide);  
        router.get("/api/calcula").handler(this::calcula);


        // Create the HTTP server and pass the "accept" method to the request handler.
        vertx.createHttpServer().requestHandler(router::accept).listen(
                config().getInteger("http.port", 8080), result -> {

                     //System.out.println(System.getenv("PBA"));
		pba=System.getenv("PBA");	

                    if (result.succeeded()) {

                        fut.complete();
                    } else {
                        fut.fail(result.cause());
                    }
                });        
        
        logger.info("Vertical iniciada !!!" + pba);
    }  
    private void primero(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        HttpServerRequest request = routingContext.request();
        String mode = request.getParam("mode");
        String jsonResponse = procesa(mode);
        response.setStatusCode(200).
        putHeader("content-type", "application/json; charset=utf-8").
        end(jsonResponse);
    }
    
    private void segundo(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        String decoded = routingContext.getBodyAsString();
        String jsonResponse = procesa(decoded);
        response.setStatusCode(200).
        putHeader("content-type", "application/json; charset=utf-8").
        end(jsonResponse);
    }


   private void resta(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        HttpServerRequest request = routingContext.request();

        String sPrimerNum  = request.getParam("primerNum");
        String sSegundoNum = request.getParam("segundoNum");
		String sOperacion  = "resta";       

		String jsonResponse = calcular(request,sPrimerNum,sSegundoNum,sOperacion);
        response.setStatusCode(200).
        putHeader("content-type", "application/json; charset=utf-8").
        end(jsonResponse);
     }


     private void calcula(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        HttpServerRequest request = routingContext.request();

		String sNumDigitos = "";
        String sNumero = request.getParam("numero");
		long lNumero = Long.parseLong(sNumero); 

        long lResulFact = factorial(lNumero);
		String sResulFact = String.valueOf(lResulFact);
		
		long lNumDigitos = sResulFact.length();
		sNumDigitos = String.valueOf(lNumDigitos);
		
		Map<Object, Object> info = new HashMap<>();
		info.put("Número", sNumero);
        info.put("Factorial", sResulFact);
		info.put("Num. Digitos del factorial", sNumDigitos);
        info.put("variable_entorno PBA ",pba);
        info.put("nombre", "Jesús");
        info.put("Current Node IP", request.localAddress().host() );

		String jsonResponse = Json.encodePrettily(info);		
        response.setStatusCode(200).
        putHeader("content-type", "application/json; charset=utf-8").
        end(jsonResponse);
     }


    public long factorial(long lNumero) {
		if (lNumero == 0) return 1;
		else return lNumero * factorial(lNumero-1);
    } 


   private void suma(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        HttpServerRequest request = routingContext.request();

        String sPrimerNum  = request.getParam("primerNum");
        String sSegundoNum = request.getParam("segundoNum");
        String sOperacion  = "suma";       

        String jsonResponse = calcular(request,sPrimerNum,sSegundoNum,sOperacion);
        response.setStatusCode(200).
        putHeader("content-type", "application/json; charset=utf-8").
        end(jsonResponse);
     }

   private void multiplica(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        HttpServerRequest request = routingContext.request();

        String sPrimerNum  = request.getParam("primerNum");
        String sSegundoNum = request.getParam("segundoNum");
        String sOperacion  = "multiplica";       

        String jsonResponse = calcular(request,sPrimerNum,sSegundoNum,sOperacion);
        response.setStatusCode(200).
        putHeader("content-type", "application/json; charset=utf-8").
        end(jsonResponse);
     }

   private void divide(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        HttpServerRequest request = routingContext.request();

        String sPrimerNum  = request.getParam("primerNum");
        String sSegundoNum = request.getParam("segundoNum");
        String sOperacion  = "divide";       

        String jsonResponse = calcular(request,sPrimerNum,sSegundoNum,sOperacion);
        response.setStatusCode(200).
        putHeader("content-type", "application/json; charset=utf-8").
        end(jsonResponse);
     }


    private String procesa(String decoded) {
        Map<String, String> autos = new HashMap<>();
        autos.put("primero", "Ferrari");
        autos.put("segundo", "Lamborgini");
        autos.put("tercero", "Bugatti");
        
        Map<Object, Object> info = new HashMap<>();
        info.put("decoded", decoded);
        info.put("nombre", "gustavo");
        info.put("edad", "21");
        info.put("autos", autos);
        info.put("variable",pba);
        return Json.encodePrettily(info);
    }


    private String calcular(HttpServerRequest request,String sPrimerNum, String sSegundoNum, String sOperacion) {

        float fPrimerNum  = Float.parseFloat(sPrimerNum);
        float fSegundoNum = Float.parseFloat(sSegundoNum);

	float fResultado = 0;

	if (sOperacion.equals("suma")){
        	fResultado = fPrimerNum + fSegundoNum;
	} else if (sOperacion.equals("resta")){
		fResultado = fPrimerNum - fSegundoNum;
	} else if (sOperacion.equals("multiplica")){
		fResultado = fPrimerNum * fSegundoNum;
	} else if (sOperacion.equals("divide")){
		fResultado = fPrimerNum / fSegundoNum; 		
	}


        Map<Object, Object> info = new HashMap<>();
        info.put("resultado", fResultado);
        info.put("variable_entorno PBA ",pba);
	info.put("nombre", "Jesús");
        info.put("Current Node IP", request.localAddress().host() );	

        return Json.encodePrettily(info);
    }



}
