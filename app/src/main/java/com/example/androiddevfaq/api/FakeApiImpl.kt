package com.example.androiddevfaq.api

import android.util.Log
import com.example.androiddevfaq.model.CategoryResponse
import com.example.androiddevfaq.model.QuestionListResponse
import com.example.androiddevfaq.model.ResponseSrc
import com.example.androiddevfaq.utils.mapper.AdapterMapper
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FakeApiImpl private constructor(
    private val delay: Long?
) : Api {

    private lateinit var categories:
            MutableMap<Category, List<Question>>
    private lateinit var fakeAnswer: String

    init {
        createData()
    }

    override suspend fun getCategories(
        isError: Boolean
    ): ResponseSrc.CategorySrc {
        delay(delay ?: 0)
//        test()
        when (isError) {
            true -> throw NoSuchFieldException("Parsing error")
            false -> {
                val keys = categories.keys.toMutableList()
                val categoriesResponse = ArrayList<CategoryResponse.CategoryItemSrc>()
                for (i in keys) {
                    categoriesResponse.add(i.toCategoryItemSrc())
                }
                return ResponseSrc.CategorySrc(true, categoriesResponse, null)
            }
        }
    }

    private fun test() {
        val currentCalendar = Calendar.getInstance()
        val newCalendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, (2018..2020).random())
            set(Calendar.MONTH, (0..11).random())
            set(Calendar.DAY_OF_MONTH, (1..28).random())
            set(Calendar.HOUR_OF_DAY, (0..24).random())
            set(Calendar.MINUTE, (0..60).random())
        }
        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
        Log.d("DateDebug", "Cal: $newCalendar")
        val userDate = simpleDateFormat.format(newCalendar.time)
        Log.d("DateDebug", "CalForm: $userDate")
        Log.d("DateDebug", "Stamp / 1000: ${newCalendar.timeInMillis}")
        Log.d("DateDebug", "Stamp: ${newCalendar.timeInMillis / 1000}")
//        val newCalendar = Calendar.getInstance()
//        newCalendar.set(Calendar.MONTH, (0..11).random())
//        newCalendar.set(Calendar.DAY_OF_MONTH, (1..28).random())
    }

    override suspend fun getQuestionListByID(
        categoryID: Int,
        isError: Boolean
    ): ResponseSrc.QuestionListSrc {
        delay(delay ?: 0)
        when (isError) {
            true -> throw NoSuchFieldException("Parsing error")
            false -> {
                val categoryKeys = categories.keys.toList()
                for (i in categoryKeys) {
                    if (i.id == categoryID) {
                        val questionListQuestion = categories[i] as ArrayList<Question>
                        val questionListResponse =
                            ArrayList<QuestionListResponse.QuestionListItemSrc>()
                        questionListQuestion.forEachIndexed { index, question ->
                            questionListResponse.add(question.toQuestionListItemSrc(index))
                        }
                        return ResponseSrc.QuestionListSrc(true, questionListResponse, null)
                    }
                }
                return ResponseSrc.QuestionListSrc(false, null, "Question List not exist")
            }
        }
    }

    override suspend fun getQuestion(
        questionID: Int,
        isError: Boolean
    ): ResponseSrc.QuestionSrc {
        delay(delay ?: 0)
        when (isError) {
            true -> throw NoSuchFieldException("Parsing error")
            false -> {
                val allQuestionsList = ArrayList<Question>()
                for (i in categories) {
                    allQuestionsList.addAll(i.value)
                }
                for (j in allQuestionsList) {
                    if (questionID == j.id) {
                        return ResponseSrc.QuestionSrc(true, j.question, j.answer, j.rating, null)
                    }
                }
                return ResponseSrc.QuestionSrc(false, null, null, null, "Question not found")
            }
        }
    }

    override suspend fun addQuestion(
        categoryID: Int,
        question: String,
        answer: String,
        isError: Boolean
    ): ResponseSrc.AddQuestionResponse {
        delay(delay ?: 0)
        when (isError) {
            true -> throw NoSuchFieldException("Parsing error")
            false -> {
                val categoryKeys = categories.keys
                for (i in categoryKeys) {
                    if (categoryID == i.id) {
                        val questionsList = categories[i]!! as ArrayList
//                        val question = Question(
//                            111,
//                            question,
//                            answer,
//                            (10..500).random(),
//                            getQuestionRandomTimeStamp()
//                        )
                        val questionItem = createQuestion(
                            111,
                            question,
                            answer
                        )
                        questionsList.add(questionItem)
                        return ResponseSrc.AddQuestionResponse(
                            true,
                            "Вопрос успешно добавлен",
                            null
                        )
                    }
                }
                return ResponseSrc.AddQuestionResponse(false, null, "Не удалось добавить вопрос")
            }
        }
    }

    private fun createData() {
        categories =
            HashMap()

        fakeAnswer =
            "AsyncTask не привязан к жизненному циклу Activity, который его содержит. Например, если вы запускаете AsyncTask внутри Activity и пользователь поворачивает устройство, активность будет уничтожена (и будет создан новый экземпляр Activity), но AsyncTask останется целым и продолжит работать до завершения.\n" +
                    "\n" +
                    "Затем, когда AsyncTask завершает работу, вместо обновления UI новой Activity, он обновляет прежний экземпляр Activity (то есть тот, в котором он был создан, но который больше не отображается!). Это может привести к исключению (типа java.lang.IllegalArgumentException: View не подключен к оконному менеджеру, если вы используете, например, findViewByIdдля получения представления внутри Activity).\n" +
                    "\n" +
                    "Также существует вероятность того, что это приведет к утечке памяти, так как AsyncTask поддерживает ссылку на Activity, что предотвращает сбор мусора, пока AsyncTask остается в живых.\n" +
                    "По этим причинам использование AsyncTasks для длительных фоновых задач, как правило – плохая идея. Для них должен использоваться другой механизм (например, служба)."

        val firstCatQuestions = getFirstCategoryQuestions()
        val maxTimestampFirst = firstCatQuestions.maxOf { it.timestamp }
        val firstCategory = createCategory(
            0,
            "Java",
            getFirstCategoryQuestions().size,
            2,
            "java_logo.png",
            maxTimestampFirst
        )

        categories[firstCategory] = firstCatQuestions

        val secondCatQuestions = getSecondCategoryQuestions()
        val maxTimestampSecond = secondCatQuestions.maxOf { it.timestamp }
        val secondCategory = createCategory(
            1,
            "Kotlin",
            getSecondCategoryQuestions().size,
            0,
            "kotlin_logo.png",
            maxTimestampSecond
        )
        categories[secondCategory] = secondCatQuestions

        val thirdCatQuestions = getThirdCategoryQuestions()
        val maxTimestampThird = secondCatQuestions.maxOf { it.timestamp }
        val thirdCategory = createCategory(
            2,
            "Android",
            getThirdCategoryQuestions().size,
            3,
            "android_logo.jpg",
            maxTimestampThird
        )
        categories[thirdCategory] = thirdCatQuestions

        val fourthCatQuestions = getFourthCategoryQuestions()
        val maxTimestampFourth = secondCatQuestions.maxOf { it.timestamp }
        val fourthCategory = createCategory(
            3,
            "Логические задачи",
            getFourthCategoryQuestions().size,
            1,
            "brain_logo.png",
            maxTimestampFourth
        )
        categories[fourthCategory] = fourthCatQuestions
    }

    private fun getFirstCategoryQuestions(): List<Question> {
        val firstCatQuestions = ArrayList<Question>()
        firstCatQuestions.add(
            createQuestion(0, "Какие бывают модификаторы доступа Java?", fakeAnswer)
        )
        firstCatQuestions.add(
            createQuestion(1, "Как работает Garbage Collector?", fakeAnswer)
        )
        firstCatQuestions.add(
            createQuestion(2, "Во что компилируются лямбда-выражения?", fakeAnswer)
        )
        return firstCatQuestions
    }

    private fun getSecondCategoryQuestions(): List<Question> {
        val secondCatQuestions = ArrayList<Question>()
        secondCatQuestions.add(
            createQuestion(3, "Что такое Kotlin Extensions?", fakeAnswer + "\n" + fakeAnswer)
        )
        secondCatQuestions.add(
            createQuestion(4, "Чем отличаются Mutable коллекции от Immutable?", fakeAnswer)
        )
        secondCatQuestions.add(
            createQuestion(5, "Зачем нужен companion object?", fakeAnswer)
        )
        secondCatQuestions.add(
            createQuestion(6, "Как обозначается функция, бросающая исключение?", fakeAnswer)
        )
        return secondCatQuestions
    }

    private fun getThirdCategoryQuestions(): List<Question> {
        val thirdCatQuestions = ArrayList<Question>()
        thirdCatQuestions.add(
            createQuestion(7, "Что такое Activity?", fakeAnswer)
        )
        return thirdCatQuestions
    }

    private fun getFourthCategoryQuestions(): List<Question> {
        val fourthCatQuestions = ArrayList<Question>()
        fourthCatQuestions.add(
            createQuestion(8, "Как решается задача ханойской башни?", fakeAnswer)
        )
        return fourthCatQuestions
    }

    private fun createCategory(
        id: Int,
        name: String,
        quantity: Int,
        priority: Int,
        logoPath: String,
        lastQuestionTimestamp: Long
    ) = Category(
        id = id,
        name = name,
        quantity = quantity,
        priority = priority,
        logoPath = logoPath,
        lastQuestionTimestamp = lastQuestionTimestamp
    )

    private fun createQuestion(id: Int, question: String, answer: String) = Question(
        id, question, answer, (0..1000).random(), getQuestionRandomTimeStamp()
    )

    private fun getQuestionRandomTimeStamp(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.YEAR, (2018..2020).random())
            set(Calendar.MONTH, (0..11).random())
            set(Calendar.DAY_OF_MONTH, (1..28).random())
            set(Calendar.HOUR_OF_DAY, (0..24).random())
            set(Calendar.MINUTE, (0..60).random())
        }.timeInMillis / 1000
    }

    private data class Category(
        val id: Int?,
        val name: String?,
        val quantity: Int?,
        val priority: Int?,
        val logoPath: String?,
        val lastQuestionTimestamp: Long?
    )

    private fun Category.toCategoryItemSrc() = CategoryResponse.CategoryItemSrc(
        id = id,
        name = name,
        quantity = quantity,
        priority = priority,
        logoPath = logoPath,
        lastQuestionDate = "Последний вопрос добавлен:\n${parseTimestampToDate(lastQuestionTimestamp ?: 0)}"
    )

    private fun parseTimestampToDate(timestamp: Long)
            = SimpleDateFormat("dd-MM-yyyy HH:mm").format(Date(timestamp * 1000))

    private data class Question(
        val id: Int,
        val question: String,
        val answer: String,
        val rating: Int,
        val timestamp: Long
    )

    private fun Question.toQuestionListItemSrc(priority: Int) =
        QuestionListResponse.QuestionListItemSrc(
            id = id,
            name = question,
            priority = priority,
            rating = rating,
            timestamp = timestamp
        )

    data class ApiBuilder(
        var _delay: Long? = null
    ) {
        fun setDelay(delay: Long?) = apply { _delay = delay }
        fun build(): FakeApiImpl {
            return FakeApiImpl(
                _delay ?: 0
            )
        }
    }
}