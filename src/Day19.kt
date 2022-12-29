import java.util.*
import kotlin.math.ceil

data class RobotCosts(val ore: Int, val clay: Int = 0, val obsidian: Int = 0)

data class Blueprint(
    val id: Int,
    val oreRobotCosts: RobotCosts,
    val clayRobotCosts: RobotCosts,
    val obsidianRobotCosts: RobotCosts,
    val geodeRobotCosts: RobotCosts
) {
    val maxOre: Int
        get() = maxOf(oreRobotCosts.ore, clayRobotCosts.ore, obsidianRobotCosts.ore, geodeRobotCosts.ore)
    val maxClay: Int
        get() = maxOf(oreRobotCosts.clay, clayRobotCosts.clay, obsidianRobotCosts.clay, geodeRobotCosts.clay)
    val maxObsidian: Int
        get() = maxOf(
            oreRobotCosts.obsidian,
            clayRobotCosts.obsidian,
            obsidianRobotCosts.obsidian,
            geodeRobotCosts.obsidian
        )

    companion object {
        fun of(input: String): Blueprint {
            val regex = Regex(
                "Blueprint (\\d+): Each ore robot costs (\\d+) ore. " +
                        "Each clay robot costs (\\d+) ore. " +
                        "Each obsidian robot costs (\\d+) ore and (\\d+) clay. " +
                        "Each geode robot costs (\\d+) ore and (\\d+) obsidian."
            )
            val costs = regex.matchEntire(input)?.groupValues?.drop(1)?.map(String::toInt) ?: error("Wrong regex pattern")
            val id = costs[0]
            val oreRobotCosts = RobotCosts(costs[1])
            val clayRobotCosts = RobotCosts(costs[2])
            val obsidianRobotCosts = RobotCosts(costs[3], clay = costs[4])
            val geodeRobotCosts = RobotCosts(costs[5], obsidian = costs[6])
            return Blueprint(id, oreRobotCosts, clayRobotCosts, obsidianRobotCosts, geodeRobotCosts)
        }
    }
}

fun main() {
    data class State(
        val timeLeft: Int,
        val ore: Int = 0,
        val oreRobots: Int = 1,
        val clay: Int = 0,
        val clayRobots: Int = 0,
        val obsidian: Int = 0,
        val obsidianRobots: Int = 0,
        val geodes: Int = 0,
        val geodeRobots: Int = 0
    ) : Comparable<State> {
        override fun compareTo(other: State): Int = compareBy<State> { it.geodes }
            .thenBy { it.geodeRobots }
            .thenBy { it.obsidianRobots }
            .thenBy { it.obsidian }
            .thenBy { it.clayRobots }
            .thenBy { it.clay }
            .thenBy { it.oreRobots }
            .thenBy { it.ore }
            .reversed()
            .thenBy { it.timeLeft }
            .compare(this, other)

        fun bestCase(): Int {
            return geodes + geodeRobots * timeLeft + timeLeft * (timeLeft - 1) / 2
        }

        fun timeUntilBuild(robot: RobotCosts): Int {
            val timeOre = when {
                (oreRobots > 0) -> ceil((robot.ore - ore) / oreRobots.toFloat()).toInt()
                (oreRobots == 0 && robot.ore <= ore) -> 0
                else -> 30
            }
            val timeClay = when {
                (clayRobots > 0) -> ceil((robot.clay - clay) / clayRobots.toFloat()).toInt()
                (clayRobots == 0 && robot.clay <= clay) -> 0
                else -> 30
            }
            val timeObsidian = when {
                (obsidianRobots > 0) -> ceil((robot.obsidian - obsidian) / obsidianRobots.toFloat()).toInt()
                (obsidianRobots == 0 && robot.obsidian <= obsidian) -> 0
                else -> 30
            }
            return maxOf(timeOre, timeClay, timeObsidian, 0) + 1
        }

        fun nextStates(blueprint: Blueprint): List<State> {
            val states = mutableListOf<State>()
            if ((blueprint.maxOre - oreRobots) * timeLeft > ore && timeLeft > 1) {
                timeUntilBuild(blueprint.oreRobotCosts).let {
                    if (it < timeLeft) {
                        states.add(State(timeLeft - it,
                            ore=ore + oreRobots * it - blueprint.oreRobotCosts.ore,
                            clay=clay + clayRobots * it,
                            obsidian=obsidian + obsidianRobots * it,
                            geodes=geodes + geodeRobots * it,
                            oreRobots = oreRobots + 1,
                            clayRobots = clayRobots,
                            obsidianRobots = obsidianRobots,
                            geodeRobots = geodeRobots
                        ))
                    }
                }
            }
            if ((blueprint.maxClay - clayRobots) * timeLeft > clay && timeLeft > 1) {
                timeUntilBuild(blueprint.clayRobotCosts).let {
                    if (it < timeLeft) {
                        states.add(State(timeLeft - it,
                            ore=ore + oreRobots * it - blueprint.clayRobotCosts.ore,
                            clay=clay + clayRobots * it,
                            obsidian=obsidian + obsidianRobots * it,
                            geodes=geodes + geodeRobots * it,
                            oreRobots = oreRobots,
                            clayRobots = clayRobots + 1,
                            obsidianRobots = obsidianRobots,
                            geodeRobots = geodeRobots
                        ))
                    }
                }
            }
            if ((blueprint.maxObsidian - obsidianRobots) * timeLeft > obsidian && timeLeft > 1) {
                timeUntilBuild(blueprint.obsidianRobotCosts).let {
                    if (it < timeLeft) {
                        states.add(State(timeLeft - it,
                            ore=ore + oreRobots * it - blueprint.obsidianRobotCosts.ore,
                            clay=clay + clayRobots * it - blueprint.obsidianRobotCosts.clay,
                            obsidian=obsidian + obsidianRobots * it,
                            geodes=geodes + geodeRobots * it,
                            oreRobots = oreRobots,
                            clayRobots = clayRobots,
                            obsidianRobots = obsidianRobots + 1,
                            geodeRobots = geodeRobots
                        ))
                    }
                }
            }
            timeUntilBuild(blueprint.geodeRobotCosts).let {
                if (it < timeLeft) {
                    states.add(State(timeLeft - it,
                        ore=ore + oreRobots * it - blueprint.geodeRobotCosts.ore,
                        clay=clay + clayRobots * it,
                        obsidian=obsidian + obsidianRobots * it - blueprint.geodeRobotCosts.obsidian,
                        geodes=geodes + geodeRobots * it,
                        oreRobots = oreRobots,
                        clayRobots = clayRobots,
                        obsidianRobots = obsidianRobots,
                        geodeRobots = geodeRobots + 1
                    ))
                }
            }
            if (states.isEmpty()) {
                states.add(State(0, geodes = geodes + geodeRobots * timeLeft))
            }
            return states
        }
    }

    val memory: MutableSet<Pair<Blueprint, State>> = mutableSetOf()
    val transitions: MutableMap<Pair<Blueprint, State>, State> = mutableMapOf()

    fun maxGeodes(blueprint: Blueprint, time: Int): Int {
        var maxGeodes = 0
        val queue = PriorityQueue<State>().apply { add(State(timeLeft = time)) }

        while(queue.isNotEmpty()) {
            val state: State = queue.poll()
            if (maxGeodes < state.bestCase()) {
                val nextStates = state.nextStates(blueprint)
                    .filter { blueprint to it !in memory }
                    .onEach { s -> memory.add(blueprint to s); transitions[blueprint to s] = state }
                queue.addAll(nextStates)
            }
            maxGeodes = maxOf(maxGeodes, state.geodes)
        }
        return maxGeodes
    }

    fun part1(): Int {
        val input = readInput("Day19")
        val blueprints = input.map { Blueprint.of(it) }
        return blueprints.sumOf { print("${it.id} "); maxGeodes(it, 24) * it.id }.also { println() }
    }

    fun part2(): Int {
        val input = readInput("Day19")
        val blueprints = input.map { Blueprint.of(it) }
        return blueprints.take(3).map { print("${it.id} "); maxGeodes(it, 32) }.reduce(Int::times).also { println() }
    }

    println(part1())
    println(part2())
}
