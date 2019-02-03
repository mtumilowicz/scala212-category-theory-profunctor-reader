import java.util.function.Function

/**
  * Created by mtumilowicz on 2019-02-03.
  */
trait Reader [R, A] extends (R => A) {
  def dimap[C, D](f: A => D, g: C => R): Reader[C, D] = rmap(f).lmap(g)
  
  def rmap[B](f: A => B): Reader[R, B] = f.compose(this).apply
  
  def lmap[B](f: B => R): Reader[B, A] = this.compose(f).apply
}
