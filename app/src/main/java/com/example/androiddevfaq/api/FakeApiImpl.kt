package com.example.androiddevfaq.api

import com.example.androiddevfaq.model.CategoryResponse
import com.example.androiddevfaq.model.QuestionListResponse
import com.example.androiddevfaq.model.ResponseSrc
import kotlinx.coroutines.delay

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
                        var questionsList = categories[i]!! as ArrayList
                        val question = Question(
                            111,
                            question,
                            answer,
                            500
                            //TODO(Hardcode rating)
                        )
                        questionsList.add(question)
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

        val firstCategory =
            Category(
                0,
                "Java",
                getFirstCategoryQuestions().size,
                2,
                "java_logo.png"
            )
        val firstCatQuestions = getFirstCategoryQuestions()
        categories[firstCategory] = firstCatQuestions

        val secondCategory =
            Category(
                1,
                "Kotlin",
                getSecondCategoryQuestions().size,
                0,
                "kotlin_logo.png"
            )
        val secondCatQuestions = getSecondCategoryQuestions()
        categories[secondCategory] = secondCatQuestions

        val thirdCategory =
            Category(
                2,
                "Android",
                getThirdCategoryQuestions().size,
                3,
                "android_logo.jpg"
            )
        val thirdCatQuestions = getThirdCategoryQuestions()
        categories[thirdCategory] = thirdCatQuestions

        val fourthCategory =
            Category(
                3,
                "Логические задачи",
                getFourthCategoryQuestions().size,
                1,
                "brain_logo.png"
            )
        val fourthCatQuestions = getFourthCategoryQuestions()
        categories[fourthCategory] = fourthCatQuestions
    }

    private fun getFirstCategoryQuestions(): List<Question> {
        val firstCatQuestions = ArrayList<Question>()
        firstCatQuestions.add(
            createQuestion(0, "Какие бывают модификаторы доступа Java?", fakeAnswer, 100)
        )
        firstCatQuestions.add(
            createQuestion(1, "Как работает Garbage Collector?", fakeAnswer, 110)
        )
        firstCatQuestions.add(
            createQuestion(2, "Во что компилируются лямбда-выражения?", fakeAnswer, 120)
        )
        return firstCatQuestions
    }

    private fun getSecondCategoryQuestions(): List<Question> {
        val secondCatQuestions = ArrayList<Question>()
        secondCatQuestions.add(
            createQuestion(3, "Что такое Kotlin Extensions?", fakeAnswer + "\n" + fakeAnswer, 130)
        )
        secondCatQuestions.add(
            createQuestion(4, "Чем отличаются Mutable коллекции от Immutable?", fakeAnswer, 140)
        )
        secondCatQuestions.add(
            createQuestion(5, "Зачем нужен companion object?", fakeAnswer, 150)
        )
        secondCatQuestions.add(
            createQuestion(6, "Как обозначается функция, бросающая исключение?", fakeAnswer, 160)
        )
        return secondCatQuestions
    }

    private fun getThirdCategoryQuestions(): List<Question> {
        val thirdCatQuestions = ArrayList<Question>()
        thirdCatQuestions.add(
            createQuestion(7, "Что такое Activity?", fakeAnswer, 170)
        )
        return thirdCatQuestions
    }

    private fun getFourthCategoryQuestions(): List<Question> {
        val fourthCatQuestions = ArrayList<Question>()
        fourthCatQuestions.add(
            createQuestion(8, "Как решается задача ханойской башни?", fakeAnswer, 170)
        )
        return fourthCatQuestions
    }

    private fun createQuestion(id: Int, question: String, answer: String, rating: Int) = Question(
        id, question, answer, rating
    )

    private data class Category(
        val id: Int?,
        val name: String?,
        val quantity: Int?,
        val priority: Int?,
        val logoPath: String?
    )

    private fun Category.toCategoryItemSrc() = CategoryResponse.CategoryItemSrc(
        id = id,
        name = name,
        quantity = quantity,
        priority = priority,
        logoPath = logoPath
    )

    private data class Question(
        val id: Int,
        val question: String,
        val answer: String,
        val rating: Int
    )

    private fun Question.toQuestionListItemSrc(priority: Int) =
        QuestionListResponse.QuestionListItemSrc(
            id = id,
            name = question,
            priority = priority,
            rating = rating
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