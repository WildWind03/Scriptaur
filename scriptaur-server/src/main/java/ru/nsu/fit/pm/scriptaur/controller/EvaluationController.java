package ru.nsu.fit.pm.scriptaur.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.pm.scriptaur.entity.MarkData;
import ru.nsu.fit.pm.scriptaur.service.EvaluationService;

@RestController
@RequestMapping("/marks")
@ComponentScan("ru.nsu.fit.pm.scriptaur.service")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity addMark(@RequestParam(value = "token") String token, @RequestBody(required = true) String data) {

        Gson gson = new Gson();
        MarkData markData = gson.fromJson(data, MarkData.class);

        //toDo: adding mark-evaluation

        return null;
    }

}
