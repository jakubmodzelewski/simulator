package com.jmodzelewski.simulator.dto;

import com.jmodzelewski.simulator.model.NodeType;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NodeDTO {
    Long id;
    String name;
    NodeType type;

    List<String> interfaces;

    //Koordynaty na polu roboczym
    int actualX;
    int actualY;
    int previousX;
    int previousY;

}
