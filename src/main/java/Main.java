import static spark.Spark.exception;
import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.webSocket;

import java.io.PrintWriter;
import java.io.StringWriter;

import environment.Player;
import environment.World;
import io.Server;
import io.Websocket;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

public class Main {
  private static final int DEFAULT_PORT = 28012;
  private World world;
  private Websocket socket;
  private Server server;

  public static void main(String[] args) {
    Main m = new Main();
    m.run(args);
  }


  private Main() {
    socket = new Websocket();
    world = new World(socket);
    socket.setWorld(world);
    server = new Server(world);
  }

  private void run(String[] args) {
    OptionParser parser = new OptionParser();
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);
    runSparkServer((int) options.valueOf("port"));
  }

  private void runSparkServer(int portNum) {
    port(portNum);
    externalStaticFileLocation("dist");
    exception(Exception.class, new ExceptionPrinter());
    webSocket("/home", socket);
    get("/hello", (req, res) -> "Hello World");
  }

  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }
}
