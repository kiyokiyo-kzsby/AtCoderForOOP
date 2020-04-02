package abc159ProblemD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Solver.getInstance(new BallStorageImpl(), new GeniusOperator(),new AnswerSheetImpl()).solve();
	}
	
	static class Solver {
		private static Solver solver;
		
		public static Solver getInstance(BallStorage ballStorage, Operator operator, AnswerSheet answerSheet) {
			if(solver==null) solver = new Solver(ballStorage,operator,answerSheet);
			return solver;
		}
		
		private final BallStorage ballStorage;
		private final Operator operator;
		private final AnswerSheet answerSheet;
		
		public Solver(BallStorage ballStorage, Operator operator,AnswerSheet answerSheet) {
			this.ballStorage = ballStorage;
			this.operator = operator;
			this.answerSheet = answerSheet;
		}
		
		public void solve() {
			operator.obtain(ballStorage.getBalls());
			while(operator.hasNextBall()) {
				answerSheet.writeDown(operator.calcNextAnswer());
			}
			answerSheet.submit();
		}
	}
	
	static interface BallStorage {
		List<Ball> getBalls();
	}
	
	static class BallStorageImpl implements BallStorage {
		
		public BallStorageImpl() {}
		
		@Override
		public List<Ball> getBalls() {
			Scanner sc = new Scanner(System.in);
			int numOfBalls = sc.nextInt();
			List<Ball> balls = new ArrayList<>();
			for(int index=1;index<=numOfBalls;index++) balls.add(new Ball(index,sc.nextInt()));
			sc.close();
			return balls;
		}
	}
	
	static interface AnswerSheet {
		void writeDown(long answer);
		void submit();
	}
	
	static class AnswerSheetImpl implements AnswerSheet{
		private List<Long> answers;
		
		public AnswerSheetImpl() {
			answers = new ArrayList<>();
		}
		
		@Override
		public void writeDown(long answer) {
			answers.add(answer);
		}

		@Override
		public void submit() {
			for(long answer : answers) System.out.println(answer);
		}	
	}
	
	static interface Operator {
		void obtain(List<Ball> balls);
		boolean hasNextBall();
		long calcNextAnswer();
	}
	
	static class GeniusOperator implements Operator{
		private List<Ball> balls;
		private int ballIndex;
		private Map<Integer,BallBox> ballBoxes;
		private long sumOfCombinationOfAllBalls;
		
		public GeniusOperator() {
			balls = new ArrayList<>();
			ballIndex = 0;
			ballBoxes = new HashMap<>();
			sumOfCombinationOfAllBalls = 0;
		}
		
		@Override
		public void obtain(List<Ball> balls) {
			this.balls = balls;
			for(Ball ball : balls) {
				if(ballBoxes.containsKey(ball.getNumber())) ballBoxes.get(ball.getNumber()).add(ball);
				else ballBoxes.put(ball.getNumber(), BallBox.createNewBoxWith(ball));
			}
			for(BallBox ballBox : ballBoxes.values()) {
				sumOfCombinationOfAllBalls += ballBox.getCombinationOfAllBalls();
			}
		}
		
		@Override
		public boolean hasNextBall() {
			return ballIndex < balls.size();
		}
		
		@Override
		public long calcNextAnswer() {
			Ball ball = balls.get(ballIndex++);
			return sumOfCombinationOfAllBalls 
					- ballBoxes.get(ball.getNumber()).getCombinationOfAllBalls()
					+ ballBoxes.get(ball.getNumber()).getCombinationOfMinusOneBalls();
		}
	}
	
	static class BallBox {
		static public BallBox createNewBoxWith(Ball ball) {
			BallBox box = new BallBox();
			box.add(ball);
			return box;
		}
		
		private List<Ball> balls;
		private boolean isCalculated;
		private long combinationOfAllBalls;
		private long combinationOfMinusOneBalls;
		
		public BallBox() {
			balls = new ArrayList<>();
			isCalculated = false;
			combinationOfAllBalls = 0;
			combinationOfMinusOneBalls = 0;
		}
		
		public void add(Ball ball) {
			balls.add(ball);
		}
		
		public long getCombinationOfAllBalls() {
			if(!isCalculated) calcCombination();
			return combinationOfAllBalls;
		}
		
		public long getCombinationOfMinusOneBalls() {
			if(!isCalculated) calcCombination(); 
			return combinationOfMinusOneBalls;
		}
		
		private void calcCombination() {
			if(balls.size()>1) {
				combinationOfAllBalls = CalcUtil.nC2(balls.size());
			}
			if(balls.size()>2) {
				combinationOfMinusOneBalls = CalcUtil.nC2(balls.size()-1);
			}
			isCalculated = true;
		}
	}
	
	static class Ball {
		private int id;
		private int number;
		
		public Ball(int id,int number) {
			this.id = id;
			this.number = number;
		}
		
		public int getNumber() {
			return number;
		}
		
		public int getId() {
			return id;
		}
		
		@Override
		public boolean equals(Object otherObject) {
			if(!(otherObject instanceof Ball)) return false;
			Ball otherBall = (Ball)otherObject;
			return id == otherBall.getId();
		}
		
		@Override
		public int hashCode() {
			return id;
		}	
	}
	
	static class CalcUtil {
		public static long nC2(int n) {
			return (long)n * (long)(n-1) / 2;
		}
	}
	
}