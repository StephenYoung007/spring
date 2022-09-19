package tech.ityoung.spring.a05.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
public class Bean1 {
    public Bean1() {
      log.info("bean1 constructed");
    }
}
