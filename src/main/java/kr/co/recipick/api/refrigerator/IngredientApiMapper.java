package kr.co.recipick.api.refrigerator;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface IngredientApiMapper {
    List<IngredientResponseVO> getIngredients(int memberId);
}