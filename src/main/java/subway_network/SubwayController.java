package subway_network;

import org.springframework.web.bind.annotation.*;
import java.io.FileNotFoundException;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class SubwayController {

    private final SubwayNetworkAnalysisTool subwayNetwork =
            new SubwayNetworkAnalysisTool();

    @PostMapping("/api/subway")
    public int calculate(@RequestBody String input)
            throws FileNotFoundException {
        
        System.out.println(input);
        return subwayNetwork.calculate(input);
    }
}