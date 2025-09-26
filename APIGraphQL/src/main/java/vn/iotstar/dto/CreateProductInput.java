package vn.iotstar.dto;
import lombok.Data;
import java.util.List;

@Data
public class CreateProductInput {
    private String title;
    private Integer quantity;
    private String desc;
    private Double price;
    private Long userId;
    private List<Long> categoryIds;
}
