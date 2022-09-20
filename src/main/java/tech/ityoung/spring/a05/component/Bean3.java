package tech.ityoung.spring.a05.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class Bean3 {
    public Bean3() {
        log.info("bean1 constructed");
    }
}
