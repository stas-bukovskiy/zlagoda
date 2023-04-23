package com.zlagoda.confiramtion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteConfirmation {

    private String id;
    private String objectName;
    private List<DeleteConfirmation> childRemovals;

}
