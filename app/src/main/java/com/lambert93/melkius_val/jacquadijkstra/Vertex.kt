package com.lambert93.melkius_val.jacquadijkstra

class Vertex(val id: String?, val name: String) {

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + (id?.hashCode() ?: 0)
        return result
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj)
            return true
        if (obj == null)
            return false
        if (javaClass != obj.javaClass)
            return false
        val other = obj as Vertex?
        if (id == null) {
            if (other!!.id != null)
                return false
        } else if (id != other!!.id)
            return false
        return true
    }

    override fun toString(): String {
        return name
    }

}