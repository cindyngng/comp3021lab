package lab11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NumbersLambda {
	
	// Find numbers with certain properties in a unified form
	// The property is specified in Predicate
	
	public static List<Integer> findNumbers(List<Integer> list, Predicate<Integer> predicate) {
		List<Integer> results = new ArrayList<Integer>();
		for (int n : list) {
			if (predicate.test(n)) results.add(n);
		}
		return results;
	}
	
	public static List<Integer> calculateScore(List<Integer> list, Function<Integer, Integer> function) {
		// TODO: Task 3
		List<Integer> results = new ArrayList<Integer>();
		for (int n : list) {
			results.add(function.apply(n));
		}
		return results;
	}
	
	public static Function<Integer, Integer> policy() {
		// TODO: Task 3
		return x->{
			int total=0;
			if (isOdd().test(x)) total=total+1;
			if (isPrime().test(x)) total=total+ 2;
			if (isPalindrome().test(x)) total=total+ 4;
			return total;
		};
	}
	
	public static Predicate<Integer> isOdd() {
		// TODO: Task 2
		return x -> x%2!=0;
	}
	
	public static Predicate<Integer> isPrime() {
		// TODO: Task 2
		return x->{
				for (int i=x-1;i>1;i--){
					if((x%i)==0){
						return false;
					}
				}
				return true;

			};
		
	}
	
	public static Predicate<Integer> isPalindrome() {
		// TODO: Task 2
		return x->{
			int k = x;
			int o=k;
			int a=0;
			while(k>0){
				int r= k %10;
				a = a* 10 +r;
				k =k/10;
			}
			if(o==a){
				return true;
			}
			return false;
		};
	}
	

	
	public static void main(String[] args) {
		List<Integer> numbers = Arrays.asList(480,514,484,389,709,935,328,169,649,300,685,429,243,532,308,87,25,282,91,415);
		
		System.out.println("The odd numbers are : " + findNumbers(numbers,isOdd()));
		System.out.println("The prime numbers are : " + findNumbers(numbers,isPrime()));
		System.out.println("The palindrome numbers are : " + findNumbers(numbers,isPalindrome()));
		
		System.out.println("The score of the all numbers are :" + calculateScore(numbers, policy()));
	}
}