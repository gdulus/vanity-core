package vanity

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes = ['left', 'right'])
class Touple<L, R> {

    final L left

    final R right

    Touple(L left, R right) {
        this.left = left
        this.right = right
    }
}
