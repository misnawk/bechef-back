package com.example.bechef.service.menuIngredient;

import com.example.bechef.model.menuIngredient.MenuIngredient;
import com.example.bechef.repository.menuIngredient.MenuIngredientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Spring의 서비스 레이어를 나타내는 어노테이션
@Transactional // 메서드가 트랜잭션 내에서 실행됨을 나타내는 어노테이션
@RequiredArgsConstructor // Lombok 어노테이션을 사용하여 final 필드 또는 @NonNull 필드를 매개변수로 가지는 생성자 자동 생성
public class MenuIngredientServiceImpl implements MenuIngredientService {

    @Autowired
    private final MenuIngredientRepository menuIngredientRepository; // MenuIngredientRepository 주입

    // 주어진 메뉴 ID와 재료 목록을 사용하여 재료를 추가하는 메서드
    @Transactional
    @Override
    public void addIngredients(int menuId, String ingredients) {
        String[] ingredientArray = ingredients.split(","); // 재료 문자열을 쉼표로 분리하여 배열로 변환

        for (String ingredient : ingredientArray) {
            String trimmedIngredient = ingredient.trim(); // 재료 문자열의 공백 제거
            MenuIngredient menuIngredient = new MenuIngredient(); // 새로운 MenuIngredient 객체 생성
            menuIngredient.setMenuId(menuId); // 메뉴 ID 설정
            menuIngredient.setIngredient(trimmedIngredient); // 재료 설정
            menuIngredientRepository.save(menuIngredient); // 재료를 데이터베이스에 저장
        }
    }

    // 주어진 메뉴 ID 목록에 해당하는 모든 재료 정보를 가져오는 메서드
    @Override
    public List<MenuIngredient> getMenuIngredientInfoByMenuId(List<Integer> menuId) {
        return menuIngredientRepository.findByMenuIdIn(menuId); // 메뉴 ID 목록에 해당하는 재료 목록을 조회하여 반환
    }
}
