fun main() {
    val children: MutableMap<String, MutableList<String>> = mutableMapOf()
    val dirs: MutableMap<String, Int> = mutableMapOf()

    fun moveUp(dir: String): String {
        val parentDir = dir.dropLast(dir.length - dir.indexOfLast { it == '/' })
        dirs[parentDir] = dirs.getOrDefault(parentDir, 0) + dirs.getOrDefault(dir, 0)
        return parentDir
    }

    fun setSystem(input: List<String>) {
        var dir = "."
        children[dir] = mutableListOf()
        for (command in input) {
            when {
                command.startsWith("\$ cd") -> {
                    when (val newDir = command.split(' ').last()) {
                        ".." -> dir = moveUp(dir)
                        "/" -> while (dir != ".") {
                            dir = moveUp(dir)
                        }

                        else -> {
                            dir = "$dir/$newDir"
                            children[dir] = mutableListOf()
                        }
                    }
                }

                command.startsWith("dir") ->
                    children[dir]?.add(command.split(' ').last()) ?: error("no dir $dir")

                command.startsWith("\$ ls") -> {}
                else -> dirs[dir] = dirs.getOrDefault(dir, 0) + command.split(' ').first().toInt()
            }
        }
        while (dir != ".") {
            dir = moveUp(dir)
        }
    }

    fun part1(): Int {
        val input = readInput("Day07")
        if (children.isEmpty()) {
            setSystem(input)
        }
        return dirs.values.filter { it <= 100000 }.sum()
    }

    fun part2(): Int {
        val input = readInput("Day07")
        if (children.isEmpty()) {
            setSystem(input)
        }
        val extra = -40000000 + dirs.getOrDefault(".", 0)
        if (extra < 0) {
            return 0
        }
        return dirs.values.filter { it >= extra }.min()
    }


    println(part1())
    println(part2())
}
