package com.jmodzelewski.simulator.dto;

import com.jmodzelewski.simulator.model.NodeType;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NodeDTO {
    Long id;
    String name;

    NodeType type;

    List<String> interfaces;
    Map<String, String> routingTable;

    //Koordynaty na polu roboczym
    int actualX;
    int actualY;
    int previousX;
    int previousY;

}
