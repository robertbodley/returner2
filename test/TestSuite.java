import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        TestStudentNumberDetectionAccuracy.class,
        TestQuizMarksDetectionAccuracy.class,
        TestScriptMarksDetectionAccuracy.class,
        TestDifferentScanners.class,
        TestRotationAngle.class,
        TestDifferentDPIAccuracy.class
})

public class TestSuite {
}

