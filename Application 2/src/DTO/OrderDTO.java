package DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Author: pasindi
 * Date: 1/7/25
 * Time: 10:30 PM
 * Description:
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDTO {
    private String orderId;
    private String customerId;
    private Date date;
}
