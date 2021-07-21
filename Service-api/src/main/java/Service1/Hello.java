package Service1;

import lombok.*;
import org.checkerframework.checker.units.qual.A;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class Hello {
    private String message;
    private String description;

}
