package kr.co.recipick.api.refrigerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientApiServiceImpl implements IngredientApiService {

    @Autowired
    private IngredientApiMapper ingredientApiMapper;

    @Override
    public List<IngredientResponseVO> getIngredients(int memberId) {
        return ingredientApiMapper.getIngredients(memberId);
    }
}