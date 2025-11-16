package com.tobi.MusicLearn_Studio_Backend.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    private List<T> content; // Danh sách dữ liệu

    private int pageNumber; // Trang hiện tại (bắt đầu từ 0)

    private int pageSize; // Số lượng items trên mỗi trang

    private long totalElements; // Tổng số items

    private int totalPages; // Tổng số trang

    private boolean first; // Có phải trang đầu tiên không

    private boolean last; // Có phải trang cuối cùng không

    private boolean empty; // Trang có rỗng không

    // Metadata bổ sung
    private Integer nextPage; // Trang tiếp theo (null nếu là trang cuối)

    private Integer previousPage; // Trang trước (null nếu là trang đầu)
}
