package ${packageController};

import ${packageService}.${serviceClassName};
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ${tableComment!}控制层
 *
 * @author ${author}
 * @since ${date}
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("${apiBasePath}")
public class ${controllerClassName} {

    private final ${serviceClassName} ${serviceClassName?uncap_first};



}
