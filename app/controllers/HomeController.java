package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.libs.Files.TemporaryFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import service.GameService;

public class HomeController extends Controller {

    private final GameService gameService;

    @Inject
    public HomeController(GameService gameService) {
        this.gameService = gameService;
    }

    public Result index() {
        
        gameService.launchTestGame(); // To test with hardcoded entry data at index rendering

        return ok(views.html.index.render());
    }

    public Result readFileAndProcess() {
        // MultipartFormData<TemporaryFile> formData = request().body().asMultipartFormData();
        // FilePart<TemporaryFile> filePart = formData.getFile("fileInput");

        // if (filePart != null) {
        //     TemporaryFile entryFile          = filePart.getRef();
        //     List<String> entryFileAsStrings  = readLinesFromFile(entryFile);

            
            // gameService.readFileAndGenerateExitFile(entryFileAsStrings);

            return ok("Le fichier d'entrée a été récupéré avec succès. Vous devriez trouver le fichier de sortie dans le dossier du fichier d'entrée.");
        // } else {
        //     return badRequest("Aucun fichier d'entrée fourni !");
        // }
    }

    // private List<String> readLinesFromFile(TemporaryFile file) {
    //     List<String> lines = new ArrayList<>();
    //     try {
    //         BufferedReader reader = new BufferedReader(new FileReader(file.getFile()));
    //         String line;
    //         while ((line = reader.readLine()) != null) {
    //             lines.add(line);
    //         }
    //         reader.close();
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    //     return lines;
    // }

}
