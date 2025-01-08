package DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: pasindi
 * Date: 1/7/25
 * Time: 10:30 PM
 * Description:
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDetailDTO {
    private String orderId;
    private String itemCode;
    private int qty;
    private double unitPrice;
}
