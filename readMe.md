# 시스템 도메인 구성도
![시스템구성도](documents/order-service-domain-logic-oncourse.png)

# graphviz(모듈 그래프 시각화 도구) 사용법

 - graphviz 를 먼저 설치해야 함
    : brew install graphviz

1) pom.xml 에 아래 플러그인 추가 

~~~xml
   <plugin>
       <groupId>com.github.ferstl</groupId>
       <artifactId>depgraph-maven-plugin</artifactId>
       <version>4.0.2</version>
       <configuration>
           <createImage>true</createImage>
           <showGroupIds>true</showGroupIds>
           <showVersions>true</showVersions>
           <showDuplicates>true</showDuplicates>
           <showConflicts>true</showConflicts>
       </configuration>
   </plugin>
~~~
2) mvn 명령어 실행
  : mvn depgraph:graph

3) order-container 하위에 dependency-graph.png 파일 생성

![그래프 결과](documents/dependency-graph.png)

# 주문 생성 요청 흐름도
![](documents/order-request-simple-flow.png)

# 주요 리팩토링
1) 복잡도 개선
 
~~~java
//order 에 제품 정보를 Restaurant 제품 기준으로 업데이트
private void setOrderProductInformationOld(Order order, Restaurant restaurant) {
    order.getItems().forEach(orderItem -> restaurant.getProducts().forEach(product -> {
        Product currentProduct = orderItem.getProduct();
        if(currentProduct.equals(product)){
            currentProduct.updateWithConfirmedNameAndPrice(product.getName(), product.getPrice());
        }
    }));
}
/*--* 워 코드는 order.getItems 를 순회하며, 내부적으로 Restaurant 의 product 를 중첩하여 탐색하면서
 * 동일한 product 를 찾는 구조로 시간복잡도가 O(n x m ) 이다. restaurant 를 Map 구조로 변환하여
 * 탐색을 O(n + m)로 변환할 수 있다. */
private void setOrderProductInformation(Order order, Restaurant restaurant) {
    Map<Product, Product> productMap = restaurant.getProducts().stream().collect(
            Collectors.toMap(product -> product, product -> product)
    );
    order.getItems().forEach(orderItem -> {
        Product currentProduct = orderItem.getProduct();
        if(productMap.containsKey(currentProduct)){
            Product restaurantProduct = productMap.get(currentProduct);
            currentProduct.updateWithConfirmedNameAndPrice(restaurantProduct.getName(), restaurantProduct.getPrice());
        }
    });
}
~~~