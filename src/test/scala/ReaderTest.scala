import org.scalatest.{FunSuite, Matchers}

/**
  * Created by mtumilowicz on 2019-01-28.
  */
class ReaderTest extends FunSuite with Matchers {
  test("size of the collection basing on the list") {
    val sizer: Reader[List[String], Int] = _.size

    sizer.dimap(_.toString, (set: Set[String]) => set.toList).apply(Set()) should be("0")
    sizer.dimap(_.toString, (set: Set[String]) => set.toList).apply(Set("a")) should be("1")
    sizer.dimap(_.toString, (set: Set[String]) => set.toList).apply(Set("a", "b", "c")) should be("3")
  }

  test("predicate: isEven") {
    val isEven: Reader[Int, Boolean] = _ % 2 == 0

    isEven.dimap(_.toString, (s: String) => s.toInt).apply("2") should be("true")
    isEven.dimap(_.toString, (s: String) => s.toInt).apply("3") should be("false")
  }

}
