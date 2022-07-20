package com.ming.snavermyshop.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ming.snavermyshop.service.KakaoUserService;
import com.ming.snavermyshop.service.ProductService;
import com.ming.snavermyshop.service.UserService;
import com.ming.snavermyshop.controller.ProductController;
import com.ming.snavermyshop.controller.UserController;
import com.ming.snavermyshop.dto.ProductRequestDto;
import com.ming.snavermyshop.model.User;
import com.ming.snavermyshop.model.UserRoleEnum;
import com.ming.snavermyshop.security.UserDetailsImpl;
import com.ming.snavermyshop.security.WebSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest( // Controller 를 테스트
        controllers = {UserController.class, ProductController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
class UserProductMvcTest {
    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean // 가짜 빈
    UserService userService;

    @MockBean
    KakaoUserService kakaoUserService;

    @MockBean
    ProductService productService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter())) // 스프링 시큐리티에 가짜 사용자 정보를 넣어주는 필터 추가함
                .build();
    }

    private void mockUserSetup() {
        // 상품 등록해보기 위해 Mock 테스트 유져 생성
        String username = "우영우";
        String password = "wutotheyoungtothewu!@#";
        String email = "wu0wu@naver.com";
        UserRoleEnum role = UserRoleEnum.USER;
        User testUser = new User(username, password, email, role);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }

    @Test
    @DisplayName("로그인 view")
    void test1() throws Exception {
        // when - then
        mvc.perform(get("/user/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 가입 요청 처리")
    void test2() throws Exception {
        // given
        MultiValueMap<String, String> signupRequestForm = new LinkedMultiValueMap<>();
        signupRequestForm.add("username", "우영우");
        signupRequestForm.add("password", "wutotheyoungtothewu!@#");
        signupRequestForm.add("email", "wu0wu@naver.com");
        signupRequestForm.add("admin", "false");

        // when - then
        mvc.perform(post("/user/signup")
                        .params(signupRequestForm)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/login"))
                .andDo(print());
    }

    @Test
    @DisplayName("신규 관심상품 등록")
    void test3() throws Exception {
        // given
        this.mockUserSetup();
        String title = "Apple <b>에어팟</b> 2세대 유선충전 모델 (MV7N2KH/A)";
        String imageUrl = "https://shopping-phinf.pstatic.net/main_1862208/18622086330.20200831140839.jpg";
        String linkUrl = "https://search.shopping.naver.com/gate.nhn?id=18622086330";
        int lPrice = 77000;
        ProductRequestDto requestDto = new ProductRequestDto(
                title,
                imageUrl,
                linkUrl,
                lPrice
        );

        String postInfo = objectMapper.writeValueAsString(requestDto); // JSON 으로 만들어 줌

        // when - then
        mvc.perform(post("/api/products")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal) // 스프링 시큐리티에서 테스트 동안에 로그인 되어있는 사용자라고 인식함
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
}