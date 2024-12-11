package adventofcode2024.utils

import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers.ofString

fun getAoC2024Input(dayNumber: String, fileName: String = "input.txt"): String {
    val file = File("src/main/kotlin/day$dayNumber/$fileName")

    if (file.exists()) {
        return file.readText().trim()
    }

    return fetchInput(dayNumber).also {
        file.writeText(it)
    }
}

private fun fetchInput(dayNumber: String): String {
    val session = File("session.txt").readText().trim()
    val dayWithoutLeadingZero = dayNumber.trimStart { it == '0' }

    val request = HttpRequest.newBuilder()
        .uri(URI.create("https://adventofcode.com/2024/day/$dayWithoutLeadingZero/input"))
        .setHeader("cookie", "session=$session")
        .GET()
        .build()

    val response = HttpClient.newHttpClient()
        .send(request, ofString())

    if (response.statusCode() != 200) {
        error(
            "Failed to get input: ${
                mapOf(
                    "Status Code" to response.statusCode(),
                    "Response Body" to response.body()
                )
            }"
        )
    }

    return response.body()
}

