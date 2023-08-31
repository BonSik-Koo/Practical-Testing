package sample.cafekiosk.spring.api.service.order.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateServiceRequest {

    /**
     * TODO: 읽기
     * Controller, Service layer 용 DTO를 분리한다.!!
     * 상의 모듈은(Controller)는 하위 모듈(Service)를 호출해야하니 존재를 알지만, 하위 모듈은 상위 모듈을 알 필요없다.
     * -> 컨트롤러, 서비스 간 DTO 때문에 의존성이 생긴다.
     *
     * 서비스 layer 전용 DTO 에는 validation 정보가 없어지니 이에 해당하는 의존성이 필요 없다.
     * -> 서비스 모듈이 커져 따로 분리해야 하는 상황에서 서비스에 validaton 의존성이 제외될 수 있음.
     *
     * 또한, 서비스가 커졌을 때 다른 API에서 해당 서비스를 호출하려면 특정 컨트롤러에 DTO 정보를 써야되고, 불필요한 의존성이 발생한다.
     */
    private List<String> productNumber;

    @Builder
    private OrderCreateServiceRequest(List<String> productNumber) {
        this.productNumber = productNumber;
    }
}
