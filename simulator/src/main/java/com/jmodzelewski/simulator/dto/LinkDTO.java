package com.jmodzelewski.simulator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LinkDTO {
    Long id;
    NodeDTO nodeA;
    NodeDTO nodeB;

    String interfaceA;
    String interfaceB;

    int xA;
    int yA;
    int xB;
    int yB;
}
