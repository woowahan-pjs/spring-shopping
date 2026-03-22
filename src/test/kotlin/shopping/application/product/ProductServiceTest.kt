package shopping.application.product

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.instancio.Instancio
import org.instancio.Select.field
import org.springframework.data.domain.PageImpl
import shopping.domain.product.Product
import shopping.domain.product.ProductBadWordClient
import shopping.domain.product.ProductRepository
import shopping.support.error.CoreException
import java.math.BigDecimal

class ProductServiceTest :
    BehaviorSpec({

        val productRepository = mockk<ProductRepository>()
        val productBadWordClient = mockk<ProductBadWordClient>()
        val productService = ProductService(productRepository, productBadWordClient)

        given("상품 목록 조회 시") {

            val page = 0
            val size = 10
            val products = Instancio.createList(Product::class.java)
            val pagedResult = PageImpl(products)

            `when`("페이징 정보를 전달하면") {
                every { productRepository.findAll(any()) } returns pagedResult

                then("페이징된 상품 목록을 반환한다") {
                    val result = productService.findByPaging(page, size)
                    result.content.size shouldBe products.size
                }
            }
        }

        given("상품 정보가 존재할 때") {

            val product =
                Instancio
                    .of(Product::class.java)
                    .set(field("name"), "정상 상품명")
                    .set(field("price"), BigDecimal.valueOf(10000))
                    .set(field("imageUrl"), "http://image.url")
                    .create()

            `when`("ID로 조회하면") {
                every { productRepository.findById(any()) } returns product

                then("상품을 반환한다") {
                    val result = productService.findById(1L)
                    result.name shouldBe "정상 상품명"
                    result.price shouldBe BigDecimal.valueOf(10000)
                }
            }
        }

        given("상품이 존재하지 않을 때") {

            `when`("ID로 조회하면") {
                every { productRepository.findById(any()) } returns null

                then("예외가 발생한다") {
                    val exception =
                        shouldThrow<CoreException> {
                            productService.findById(1L)
                        }
                    exception.message shouldBe "상품이 존재하지 않습니다."
                }
            }
        }

        given("상품을 생성할 때") {

            `when`("비속어가 포함되지 않은 정상 상품명이면") {
                val name = "정상 상품"
                val price = BigDecimal.valueOf(10000)
                val imageUrl = "http://image.url"
                val product = Product(name, price, imageUrl)

                every { productBadWordClient.containsProfanity(name) } returns false
                every { productRepository.save(any()) } returns product

                then("상품을 저장하고 반환한다") {
                    val result = productService.create(name, price, imageUrl)
                    result.name shouldBe name
                    verify { productRepository.save(any()) }
                }
            }

            `when`("상품명에 비속어가 포함되어 있으면") {
                val name = "비속어포함상품"

                every { productBadWordClient.containsProfanity(name) } returns true

                then("예외가 발생한다") {
                    val exception =
                        shouldThrow<CoreException> {
                            productService.create(name, BigDecimal.valueOf(10000), "http://image.url")
                        }
                    exception.message shouldBe "상품 이름에 비속어를 포함할 수 없습니다."
                }
            }
        }

        given("상품을 수정할 때") {

            val existingProduct = Product("기존 이름", BigDecimal.valueOf(5000), "http://old.url")

            `when`("정상적으로 수정 요청하면") {
                val newName = "수정 이름"
                val newPrice = BigDecimal.valueOf(10000)
                val newImageUrl = "http://new.url"

                every { productRepository.findById(any()) } returns existingProduct
                every { productBadWordClient.containsProfanity(newName) } returns false
                every { productRepository.save(any()) } returnsArgument 0

                then("상품 정보가 수정되고 저장된다") {
                    val result = productService.update(1L, newName, newPrice, newImageUrl)
                    result.name shouldBe newName
                    result.price shouldBe newPrice
                    result.imageUrl shouldBe newImageUrl
                }
            }
        }

        given("상품을 삭제할 때") {

            val product = Instancio.create(Product::class.java)

            `when`("ID가 존재하면") {
                every { productRepository.findById(any()) } returns product
                every { productRepository.deleteById(any()) } returns Unit

                then("상품이 삭제된다") {
                    productService.delete(product.id)
                    verify { productRepository.deleteById(product.id) }
                }
            }
        }
    })
