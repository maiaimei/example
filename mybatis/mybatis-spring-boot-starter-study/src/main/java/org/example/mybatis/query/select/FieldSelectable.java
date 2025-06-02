package org.example.mybatis.query.select;

import java.util.List;
import org.example.mybatis.query.Queryable;

public interface FieldSelectable extends Queryable {

  List<String> getSelectFields();
}
