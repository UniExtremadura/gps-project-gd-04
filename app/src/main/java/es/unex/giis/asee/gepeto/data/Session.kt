package es.unex.giis.asee.gepeto.data

object Session {
    private var data = HashMap<String, Any>()

    fun getValue(key: String): Any? {
        return data[key]
    }

    fun setValue(key: String, value: Any) {
        data[key] = value
    }
}