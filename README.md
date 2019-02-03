# scala212-category-theory-profunctor-reader
_Reference_: https://ocharles.org.uk/blog/guest-posts/2013-12-22-24-days-of-hackage-profunctors.html  
_Reference_: https://bartoszmilewski.com/2015/02/03/functoriality/  
_Reference_: https://bartoszmilewski.com/2016/07/25/profunctors-as-relations/

# preface
Please refer my other github project: 

A **profunctor** is a functor that
* is contravariant in its first argument (https://github.com/mtumilowicz/scala212-category-theory-contravariant-op-functor)
* covariant in the second (covariant = ordinary functor)
* target category is **Set**
* summary: `Cop x D -> Set`, where `Cop` is opposite category to C

```
class Profunctor p where
  dimap :: (a -> b) -> (c -> d) -> p b c -> p a d
  dimap f g = lmap f . rmap g
  lmap :: (a -> b) -> p b c -> p a c
  lmap f = dimap f id
  rmap :: (b -> c) -> p a b -> p a c
  rmap = dimap id
```

Just like with `Bifunctor` (https://github.com/mtumilowicz/scala212-category-theory-either-bifunctor)
we could either implement dimap (and accepting defaults for lmap
and rmap), or implement lmap and rmap and accept default for dimap,
or specify three of them, but assure that they are related in 
proper way.

# profunctor as relation

# project description
* haskell is extremely expressive
    ```
    instance Profunctor (->) where
      dimap ab cd bc = cd . bc . ab
      lmap = flip (.)
      rmap = (.)
    ```
* and here comes the Scala code
    ```
    trait Reader [R, A] extends (R => A) {
      def dimap[C, D](f: A => D, g: C => R): Reader[C, D] = rmap(f).lmap(g)
      
      def rmap[B](f: A => B): Reader[R, B] = f.compose(this).apply
      
      def lmap[B](f: B => R): Reader[B, A] = this.compose(f).apply
    }
    ```