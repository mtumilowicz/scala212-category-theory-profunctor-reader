# scala212-category-theory-profunctor-reader
_Reference_: https://ocharles.org.uk/blog/guest-posts/2013-12-22-24-days-of-hackage-profunctors.html  
_Reference_: https://bartoszmilewski.com/2015/02/03/functoriality/  
_Reference_: https://bartoszmilewski.com/2016/07/25/profunctors-as-relations/

# preface
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
a proper way.

# hom-functor
* hom-set `C(a, b)` is a set of morphisms between `a` and `b` 
(in category `C`)
* **hom-functor** takes a pair of objects `a` and `b` and assigns 
to it the set of morphisms between them -  
the hom-set `C(a, b)`
* `Cop x C -> Set` (`C(a, b) e Set`)
* we want to define its action on morphisms:
    * morphism in `Cop x C` is a pair of morphisms
        * `f: a' -> a`
        * `g: b -> b'`
    * take any `h e C(a, b)`, 
    then `g . h . f e C(a', b')`

# profunctor as relation

## relation
* a relation between two sets is a subset of the 
cartesian product of two sets
* or could be defined as a function on the cartesian 
product of two sets - a function 
that assigns zero (or false) to those pairs that are 
not in a relation, and one (or true) to those which are

## intuition
* we will try to extend above reasoning to categories and profunctors
* take declaration: `Cop x D -> Set`
* why the target category is **Set**? 
    * because we can think of a
        relation as a function that assigns empty set if the relation
        does not exist and singleton if elements are related
    * hom-set belongs to **Set**: `C(a, b) e Set`
    * consider preorder category -> hom-set is either empty or is a singleton
        which has direct correspondence with above reasoning
* why morphisms in the first argument are mapped contravariantly?
    * suppose we are in preorder category
    * suppose we have `Cop x C -> Set`
    * take `a` and `b` and suppose `C(a, b)` is a singleton, so
    we have single morphism `r: a -> b`
    * take a morphism that goes from (a, b) to (a', b'), 
    it is a pair of morphisms:
        * `f: a' -> a`
        * `g: b -> b'`
    * the composition `g . r . f e C(a', b')`
    * in case of `C x C -> Set` there will be no general way of
    constructing morphism from `C(a', b')`
* in general we can think about hom-set as a:
    * empty hom-set always means that there is no relation
    * not empty hom-set means that there are (multiple) proof
    of relation
    

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