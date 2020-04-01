package abc159ProblemD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Solver.getInstance().solve();
	}
	
	static class Solver {
		private static Solver solver = new Solver();
		
		public static Solver getInstance() {
			return solver;
		}
		
		private BallStorage ballStorage;
		private Operator operator;
		
		public Solver() {
			this.ballStorage = new BallStorageImpl();
			this.operator = new Operator();
		}
		
		public void solve() {
			List<Ball> balls = ballStorage.getBalls();
			operator.arrange(balls);
			for(Ball ball : balls) operator.calcCombinationWithout(ball);
			operator.submitAnswer();
		}
	}
	
	static interface BallStorage {
		public List<Ball> getBalls();
	}
	
	static class BallStorageImpl implements BallStorage {
		private List<Ball> balls;
		
		public BallStorageImpl() {
			Scanner sc = new Scanner(System.in);
			int numOfBalls = sc.nextInt();
			this.balls = new ArrayList<>();
			for(int index=1;index<=numOfBalls;index++) balls.add(new Ball(sc.nextInt()));
			sc.close();
		}
		
		@Override
		public List<Ball> getBalls() {
			return this.balls;
		}
	}
	
	static interface AnswerSheet {
		public void writeDown(long answer);
		public void submit();
	}
	
	static class AnswerSheetImpl implements AnswerSheet{
		private List<Long> answers;
		
		public AnswerSheetImpl() {
			this.answers = new ArrayList<>();
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
	
	static class Operator {
		private Map<Integer,BallBox> ballBoxes;
		private long sumOfCombinationOfAllBalls;
		private AnswerSheet answerSheet;
		
		public Operator() {
			this.ballBoxes = new HashMap<>();
			this.sumOfCombinationOfAllBalls = 0;
			this.answerSheet = new AnswerSheetImpl();
		}
		
		public void arrange(List<Ball> balls) {
			for(Ball ball : balls) {
				if(ballBoxes.containsKey(ball.getNumber())) ballBoxes.get(ball.getNumber()).add(ball);
				else ballBoxes.put(ball.getNumber(), BallBox.getNewBox(ball));
			}
			for(BallBox ballBox : ballBoxes.values()) {
				this.sumOfCombinationOfAllBalls += ballBox.getCombinationOfAllBalls();
			}
		}
		
		public void calcCombinationWithout(Ball ball) {
			long combinationWithoutSpecificBall = sumOfCombinationOfAllBalls 
					- ballBoxes.get(ball.getNumber()).getCombinationOfAllBalls()
					+ ballBoxes.get(ball.getNumber()).getCombinationOfMinusOneBalls();
			answerSheet.writeDown(combinationWithoutSpecificBall);
		}
		
		public void submitAnswer() {
			answerSheet.submit();
		}
	}
	
	
	static class BallBox {
		static public BallBox getNewBox(Ball ball) {
			BallBox box = new BallBox();
			box.add(ball);
			return box;
		}
		
		private List<Ball> balls;
		private boolean isCalculated;
		private long combinationOfAllBalls;
		private long combinationOfMinusOneBalls;
		
		public BallBox() {
			this.balls = new ArrayList<>();
			this.isCalculated = false;
			this.combinationOfAllBalls = 0;
			this.combinationOfMinusOneBalls = 0;
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
		private int number;
		
		public Ball(int number) {
			this.number = number;
		}
		
		public int getNumber() {
			return this.number;
		}
		
	}
	
	static class CalcUtil {
		public static long nC2(int n) {
			return (long)n * (long)(n-1) / 2;
		}
	}
	
}