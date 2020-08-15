package com.jmodzelewski.simulator.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouterDTO {
    protected Long id;

    int actualX;
    int actualY;
    int previousX;
    int previousY;

}
