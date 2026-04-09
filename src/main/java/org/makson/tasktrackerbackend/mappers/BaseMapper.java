package org.makson.tasktrackerbackend.mappers;

import java.util.List;

public interface BaseMapper<M, D> {
    D toDto(M model);

    M toModel(D dto);

    List<D> toListDto(List<M> models);

    List<M> toListModel(List<D> dto);

}
