package com.jmodzelewski.simulator.controllers.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
class NodeDTO {
    String id = null;
    String name = null;

    int x = 0;
    int y = 0;
}
