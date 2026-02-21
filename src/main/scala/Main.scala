object Main extends App {
  println("Hello, Scala!")

  val age: Int = 42
  val workAge: Int = 24

  def sum(age: Int, workAge: Int): String = {
    val sumYears = age + workAge

    s"The age is $sumYears"
  }
  var interation: Int = 1
  val maxNumber: Int = 42
  def goldenRatio(): Unit = {
    if(interation > maxNumber) return
    else {
      println(interation)
      interation = interation*2
      goldenRatio()
    }


  }

  println(sum(age, workAge))
  goldenRatio()
}