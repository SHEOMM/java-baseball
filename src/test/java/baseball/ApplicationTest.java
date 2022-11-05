package baseball;

import baseball.UI.InputView;
import baseball.UI.ResultView;
import baseball.model.Hint;
import baseball.model.Input;
import baseball.model.Result;
import baseball.model.State;
import baseball.service.RandomNumberListGenerator;
import baseball.service.Service;
import camp.nextstep.edu.missionutils.test.NsTest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

import static camp.nextstep.edu.missionutils.test.Assertions.assertRandomNumberInRangeTest;
import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.in;

class ApplicationTest extends NsTest {
    @Test
    void 게임종료_후_재시작() {
        assertRandomNumberInRangeTest(
                () -> {
                    run("246", "135", "1", "597", "589", "2");
                    assertThat(output()).contains("낫싱", "3스트라이크", "1볼 1스트라이크", "3스트라이크", "게임 종료");
                },
                1, 3, 5, 5, 8, 9
        );
    }

    @Test
    void 예외_테스트() {
        assertSimpleTest(() ->
                assertThatThrownBy(() -> runException("1234"))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    void test_Input_String_toIntList(){
        String test = "123";
        List<Integer> testList = new Input(test).getBaseballNumberList();
        assertThat(testList).containsExactly(1, 2, 3);
    }



    @Test
    void test_InputView(){
        String input = "123";
        InputStream in = new ByteArrayInputStream(input.getBytes());;
        System.setIn(in);
        InputView inputView = new InputView();
        inputView.getBaseballInput();
        assertThat(inputView.getInput().getBaseballNumberList())
                .containsExactly(1, 2, 3);
    }

    @Test
    void test_Hint_Model_ToString(){
        int ball = 2;
        int strike = 1;
        Hint hint = new Hint(ball, strike);
        assertThat(hint.toString()).isEqualTo("2볼 1스트라이크\n");
    }

    @Test
    void test_ResultView_FINISH(){

        String input = "1";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ResultView resultView = new ResultView();
        resultView.printResult(State.FINISH);
        assertThat(resultView.getResult()).isEqualTo(Result.FINISH);
    }
    @Test
    void test_ResultView_Error(){
        ResultView resultView = new ResultView();
        assertSimpleTest(() ->
                assertThatThrownBy(() -> resultView.printResult(State.ERROR))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    void test_Service_GenerateHint_3strike(){
        Service service = new Service();
        String answer = getAnswerString(service);
        Input input = new Input(answer);
        assertThat(service.generateHint(input).toString())
                .isEqualTo("3스트라이크\n");
    }

    @Test
    void test_Service_GenerateHint_2Ball_1Strike(){
        Service service = new Service();
        String answer = getAnswerString(service);
        char[] charInput = answer.toCharArray();
        char swapNum = charInput[2];
        charInput[2] = charInput[0];
        charInput[0] = swapNum;
        String inputChar = new String(charInput);
        Input input = new Input(inputChar);
        assertThat(service.generateHint(input).toString())
                .isEqualTo("2볼 1스트라이크\n");
    }


    private String getAnswerString(Service service){
        return service.getAnswer()
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}
