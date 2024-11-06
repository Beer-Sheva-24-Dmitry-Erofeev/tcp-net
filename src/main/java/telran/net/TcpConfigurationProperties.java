package telran.net;

// Интерфейс, чтобы просто хранить настройки
// Почему мы выбрали для этого именно интерфейс а не класс?
public interface TcpConfigurationProperties {

    String REQUEST_TYPE_FIELD = "requestType"; // Тип запроса
    String REQUEST_DATA_FIELD = "requestData"; // Данные запроса
    String RESPONSE_CODE_FIELD = "responseCode"; // Код ответа
    String RESPONSE_DATA_FIELD = "responseData"; // Данные ответа
    int DEFAULT_INTERVAL_CONNECTION = 3000; // Пауза между попытками подключения в мс по умолчанию
    int DEFAULT_TRIALS_NUMBER_CONNECTION = 10; // Количество попыток подключения по умолчанию
}
