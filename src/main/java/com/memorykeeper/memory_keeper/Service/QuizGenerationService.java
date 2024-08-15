package com.memorykeeper.memory_keeper.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.completion.chat.*;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.memorykeeper.memory_keeper.model.QuizQuestion;
import com.memorykeeper.memory_keeper.model.QuizResult;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class QuizGenerationService {

    private final OpenAiService openAiService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, QuizResult> quizResults = new HashMap<>();

    public QuizGenerationService(@Value("${openai.api.key}") String apiKey) {
        // OkHttpClient with custom timeout settings
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)  // 연결 타임아웃
                .writeTimeout(60, TimeUnit.SECONDS)    // 쓰기 타임아웃
                .readTimeout(60, TimeUnit.SECONDS)     // 읽기 타임아웃
                .build();
        this.openAiService = new OpenAiService(apiKey);
    }

    public List<QuizQuestion> generateCognitiveQuizFromJsonFile(String jsonFilePath) {
        List<QuizQuestion> allQuestions = new ArrayList<>();
        String[] cognitiveFunctions = {"언어능력", "시공간 능력", "실행판단 계획능력", "지남력"};

        try {
            JsonNode rootNode = objectMapper.readTree(new File(jsonFilePath));

            String categoryName = rootNode.path("category").path("ctg_nm_level3").asText();
            String gender = rootNode.path("abstract_image").path("gender").asText();
            int age = rootNode.path("abstract_image").path("age").asInt();
            String proficiency = rootNode.path("abstract_image").path("proficiency").asText();
            String imageUrl = rootNode.path("abstract_image").path("abs_path").asText();

            for (String function : cognitiveFunctions) {
                String prompt = String.format(
                        "한국어로 3개의 인지능력 퀴즈를 생성해주세요. 그리고 2개는 보이스피싱 지식에 관한 문제를 내주세요. 각 문제는 질문, 3개의 선택지, 그리고 정답 번호(1, 2, 3 중 하나)로 구성되어야 합니다. " +
                                "퀴즈는 %d세 %s의 %s 능력에 맞춰야 합니다. 제품 카테고리는 %s입니다.",
                        age, gender.equals("F") ? "여성" : "남성", proficiency, categoryName
                );

                ChatMessage message = new ChatMessage("user", prompt);
                ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                        .model("gpt-3.5-turbo")  // 또는 gpt-4
                        .messages(Collections.singletonList(message))
                        .maxTokens(500)
                        .temperature(0.7)
                        .build();

                ChatCompletionResult chatCompletionResult = openAiService.createChatCompletion(chatCompletionRequest);
                String generatedText = chatCompletionResult.getChoices().get(0).getMessage().getContent().trim();

                allQuestions.addAll(parseGeneratedQuiz(generatedText, imageUrl, function));
            }

            return allQuestions;

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<QuizQuestion> parseGeneratedQuiz(String generatedText, String imageUrl, String cognitiveFunction) {
        List<QuizQuestion> quizQuestions = new ArrayList<>();
        String[] questions = generatedText.split("\n\n"); // 문제와 선택지 구분, 예시용

        for (String questionBlock : questions) {
            String[] lines = questionBlock.split("\n");

            // Ensure that the lines array has at least 5 elements before accessing lines[4]
            if (lines.length >= 5) {
                QuizQuestion question = new QuizQuestion();
                question.setQuestionText(lines[0]); // 첫 줄을 질문으로 가정
                List<String> options = Arrays.asList(lines[1], lines[2], lines[3]);
                question.setOptions(options);

                // 정답 추출 (정규 표현식 사용)
                String correctAnswerText = lines[4].replaceAll("[^0-9]", ""); // 숫자만 추출

                try {
                    int correctAnswer = Integer.parseInt(correctAnswerText); // 정수를 파싱
                    question.setCorrectAnswer(correctAnswer); // 정답 설정
                } catch (NumberFormatException e) {
                    System.out.println("Failed to parse correct answer: " + correctAnswerText);
                    continue; // 파싱 실패 시 이 질문을 건너뜀
                }

                question.setImageUrl(imageUrl);
                question.setCognitiveFunction(cognitiveFunction);
                quizQuestions.add(question);
            } else {
                System.out.println("Skipping block: not enough lines (" + lines.length + ")");
            }
        }

        return quizQuestions;
    }

    public void storeQuizResult(String cognitiveFunction, boolean isCorrect) {
        QuizResult result = quizResults.getOrDefault(cognitiveFunction, new QuizResult(cognitiveFunction, 0, 0));
        result.setTotalQuestions(result.getTotalQuestions() + 1);
        if (isCorrect) {
            result.setCorrectAnswers(result.getCorrectAnswers() + 1);
        }
        quizResults.put(cognitiveFunction, result);
    }

    public List<QuizResult> getQuizResults() {
        return new ArrayList<>(quizResults.values());
    }
}
