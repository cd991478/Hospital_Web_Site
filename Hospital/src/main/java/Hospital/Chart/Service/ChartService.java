package Hospital.Chart.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Hospital.D_Hospital.Entity.D_Hospital;
import Hospital.D_Hospital.Entity.D_HospitalRepository;
import Hospital.D_Hospital.Service.D_HospitalService;
import Hospital.Patient.Entity.Patient;
import Hospital.Patient.Service.PatientService;

@Service
public class ChartService {
	
	@Autowired
	private PatientService ps;
	@Autowired
	private D_HospitalService d_hs;
	@Autowired
	private D_HospitalRepository d_hr;
	
	public Map<String, Object> GetAllChart(String Gender, String AgeRange){
         
         Map<String, Object> stats = new HashMap<>();
         
         Integer Total=0;/*문진표DB 총합*/ 
         Integer Male=0;/*남성*/ Integer Female=0;/*여성*/ 
         
         Integer Age_0to9=0;/*10살 미만*/ Integer Age_10to19=0;/*10대*/ 
         Integer Age_20to29=0;/*20대*/ Integer Age_30to39=0;/*30대*/ 
         Integer Age_40to49=0;/*40대*/ Integer Age_50to59=0;/*50대*/ 
         Integer Age_60to69=0;/*60대*/ Integer Age_70to99=0;/*70대 이상*/ 
         /*약복용 유무*/ 
         Integer TakingPill_Yes=0;Integer TakingPill_No=0;
         /*증상 분류*/ 
         Integer Nose=0;/*콧물/코막힘*/ Integer Cough=0;/*기침*/ 	         
         Integer Pain=0;/*통증(두통/흉통 등)*/Integer Diarrhea=0;/*설사*/ 
         Integer Nothing=0;/*해당사항 없음*/ 
         /*고위험군 분류*/ 
         Integer HighRiskGroup_Under59=0;/*59개월 미만*/ 
         Integer HighRiskGroup_Pregnancy=0;/*임산부*/ 
         Integer HighRiskGroup_Lung=0;/*만성 폐질환*/ 
         Integer HighRiskGroup_Diebete=0;/*당뇨*/ 
         Integer HighRiskGroup_Cancer=0;/*암환자*/ 
         Integer HighRiskGroup_None=0;/*해당사항없음*/ 
         /*시각통증척도 0~10*/
         Integer VAS_0=0;Integer VAS_1=0;	        
         Integer VAS_2=0;Integer VAS_3=0;	         
         Integer VAS_4=0;Integer VAS_5=0;	         
         Integer VAS_6=0;Integer VAS_7=0; 
         Integer VAS_8=0;Integer VAS_9=0;
         Integer VAS_10=0;Integer VAS_Sum=0;
         double VAS_Avg=0.00;/*시각통증척도 평균 0*/
         
         List<Patient> patient = ps.findAll();
         // 성별 및 나이대에 맞는 환자들만 필터링
          for (Patient p : patient) {
              boolean genderMatch = true; boolean ageMatch = true;
              // 성별 필터링
				  if (Gender != null) {						 
					  String GenderNumber;
					  if(p.getP_Gender() == 0) {GenderNumber = "M";}
					  else {GenderNumber = "F";}					
					  genderMatch = GenderNumber.equals(Gender);
				  }
              // 나이대 필터링
              if (AgeRange != null && !AgeRange.isEmpty()) {
                  int age = p.getP_Age();//DB에 있는 모든 환자 정보를 가져와 분류
                  switch (AgeRange) {
                      case "0to9":ageMatch = (age >= 0 && age < 10);
                          break;
                      case "10to19":ageMatch = (age >= 10 && age < 20);
                          break;
                      case "20to29":ageMatch = (age >= 20 && age < 30);
                          break;
                      case "30to39":ageMatch = (age >= 30 && age < 40);
                          break;
                      case "40to49":ageMatch = (age >= 40 && age < 50);
                          break;
                      case "50to59":ageMatch = (age >= 50 && age < 60);
                          break;
                      case "60to69":ageMatch = (age >= 60 && age < 70);
                          break;
                      case "70to99":ageMatch = (age >= 70 && age < 100);
                          break;
                      default:ageMatch = true;  // 나이대에 제한이 없으면 필터링하지 않음
                  }
              }

              // 성별과 나이대가 모두 일치하는 경우에만 데이터 처리
              if (genderMatch && ageMatch) {
                  Total++;
                  // 성별 통계
                  if (p.getP_Gender() == 1) {Female++;} else {Male++;}
                  // 나이대 통계
                  if (p.getP_Age() >= 0 && p.getP_Age() < 10) {
                      Age_0to9++;//10세 미만
                  } else if (p.getP_Age() >= 10 && p.getP_Age() < 20) {
                      Age_10to19++;//10대
                  } else if (p.getP_Age() >= 20 && p.getP_Age() < 30) {
                      Age_20to29++;//20대
                  } else if (p.getP_Age() >= 30 && p.getP_Age() < 40) {
                      Age_30to39++;//30대
                  } else if (p.getP_Age() >= 40 && p.getP_Age() < 50) {
                      Age_40to49++;//40대
                  } else if (p.getP_Age() >= 50 && p.getP_Age() < 60) {
                      Age_50to59++;//50대
                  } else if (p.getP_Age() >= 60 && p.getP_Age() < 70) {
                      Age_60to69++;//60대
                  } else {Age_70to99++;/*70대(나머지)*/ }

                  // 기타 통계는 기존의 코드와 동일하게 계산
                  if (p.getP_TakingPill() == 1) {TakingPill_Yes++;} 
                  else {TakingPill_No++;}
                  
                  if (p.getP_Nose() == 1) {Nose++;}
                  if (p.getP_Cough() == 1) {Cough++;}
                  if (p.getP_Pain() == 1) {Pain++;}
                  if (p.getP_Diarrhea() == 1) {Diarrhea++;}
                  if (p.getP_Nose() == 0 && p.getP_Cough() == 0 
                	  && p.getP_Pain() == 0 && p.getP_Diarrhea() == 0) {
                      Nothing++;}
                  //고위험군 카운트하기
                  if (p.getP_HighRiskGroup() == 0) {HighRiskGroup_Under59++;} 
                  else if (p.getP_HighRiskGroup() == 1) {HighRiskGroup_Pregnancy++;} 
                  else if (p.getP_HighRiskGroup() == 2) {HighRiskGroup_Lung++;} 
                  else if (p.getP_HighRiskGroup() == 3) {HighRiskGroup_Diebete++;} 
                  else if (p.getP_HighRiskGroup() == 4) {HighRiskGroup_Cancer++;} 
                  else if (p.getP_HighRiskGroup() == 5) {HighRiskGroup_None++;}
                  //환자별 VAS 점수 카운트
                  if (p.getP_VAS() == 0) {VAS_0++;} 
                  else if (p.getP_VAS() == 1) {VAS_1++;} 
                  else if (p.getP_VAS() == 2) {VAS_2++;} 
                  else if (p.getP_VAS() == 3) {VAS_3++;} 
                  else if (p.getP_VAS() == 4) {VAS_4++;} 
                  else if (p.getP_VAS() == 5) {VAS_5++;} 
                  else if (p.getP_VAS() == 6) {VAS_6++;} 
                  else if (p.getP_VAS() == 7) {VAS_7++;} 
                  else if (p.getP_VAS() == 8) {VAS_8++;} 
                  else if (p.getP_VAS() == 9) {VAS_9++;} 
                  else {VAS_10++;}
                  VAS_Sum += p.getP_VAS();
              }
          }
         if(Total > 0) {
            VAS_Avg = (double)((double)VAS_Sum / (double)Total);
            VAS_Avg = Math.round(VAS_Avg * 100.0) / 100.0;
         }
         //카운트된 성별을 Map에 투입
         stats.put("Total", Total);
         stats.put("Male", Male);
         stats.put("Female", Female);
         //카운트된 연령대를 Map에 투입
         stats.put("Age_0to9", Age_0to9);
         stats.put("Age_10to19", Age_10to19);
         stats.put("Age_20to29", Age_20to29);
         stats.put("Age_30to39", Age_30to39);
         stats.put("Age_40to49", Age_40to49);
         stats.put("Age_50to59", Age_50to59);
         stats.put("Age_60to69", Age_60to69);
         stats.put("Age_70to99", Age_70to99);
         //카운트된 약복용 유무를 Map에 투입
         stats.put("TakingPill_Yes", TakingPill_Yes);
         stats.put("TakingPill_No", TakingPill_No);
         //카운트된 증상을 Map에 투입
         stats.put("Nose", Nose);
         stats.put("Cough", Cough);
         stats.put("Pain", Pain);
         stats.put("Diarrhea", Diarrhea);
         stats.put("Nothing", Nothing);
         //카운트된 고위험군 분류를 Map에 투입
         stats.put("HighRiskGroup_Under59", HighRiskGroup_Under59);
         stats.put("HighRiskGroup_Pregnancy", HighRiskGroup_Pregnancy);
         stats.put("HighRiskGroup_Lung", HighRiskGroup_Lung);
         stats.put("HighRiskGroup_Diebete", HighRiskGroup_Diebete);
         stats.put("HighRiskGroup_Cancer", HighRiskGroup_Cancer);
         stats.put("HighRiskGroup_None", HighRiskGroup_None);
         //카운트된 VAS값을 Map에 투입
         stats.put("VAS_0", VAS_0);
         stats.put("VAS_1", VAS_1);
         stats.put("VAS_2", VAS_2);
         stats.put("VAS_3", VAS_3);
         stats.put("VAS_4", VAS_4);
         stats.put("VAS_5", VAS_5);
         stats.put("VAS_6", VAS_6);
         stats.put("VAS_7", VAS_7);
         stats.put("VAS_8", VAS_8);
         stats.put("VAS_9", VAS_9);
         stats.put("VAS_10", VAS_10);
         stats.put("VAS_Sum", VAS_Sum);
         stats.put("VAS_Avg", VAS_Avg);
         
         return stats;
      }
	
	public Map<String, Object> GetAllHospitalRegion(){
        Map<String, Object> stats = new HashMap<>();
		Integer Suseong = 0;/*수성구*/ Integer Dalseo = 0;/*달서구*/ 
        Integer Joong = 0;/*중구*/ Integer Dong = 0;/*동구*/ 
        Integer Seo = 0;/*서구*/ Integer Nam = 0;/*남구*/ 
        Integer Book = 0;/*북구*/ Integer Dalseong = 0;/*달성군*/ 
        Integer Goonwe = 0;/*군위군*/ Integer Total = 0;/*합계*/ 
        
        List<D_Hospital> AllHospital = d_hs.findAll();
        for(D_Hospital hospital : AllHospital) {
           String FullAddress = hospital.getH_Address();
           String FirstAddress;
           String SecondAddress;
           System.out.println(FullAddress);
           if(FullAddress.contains(" ")) {
              FirstAddress = FullAddress.split(" ")[0];
              SecondAddress = FullAddress.split(" ")[1]; 
              System.out.println(SecondAddress);
           }
           else {
              FirstAddress = "";
              SecondAddress = "";
           }
           
           Total ++;
           
           if(SecondAddress.equals("수성구")) {
              Suseong++;
           }
           else if(SecondAddress.equals("달서구")) {
              Dalseo++;
           }
           else if(SecondAddress.equals("중구")) {
              Joong++;
           }
           else if(SecondAddress.equals("동구")) {
              Dong++;
           }
           else if(SecondAddress.equals("서구")) {
              Seo++;
           }
           else if(SecondAddress.equals("남구")) {
              Nam++;
           }
           else if(SecondAddress.equals("북구")) {
              Book++;
           }
           else if(SecondAddress.equals("달성군")) {
              Dalseong++;
           }
           
           if(FirstAddress.equals("군위군")) {
              Goonwe++;
           }
           
        }
        
        stats.put("Suseong", Suseong);
        stats.put("Dalseo", Dalseo);
        stats.put("Joong", Joong);
        stats.put("Dong", Dong);
        stats.put("Seo", Seo);
        stats.put("Nam", Nam);
        stats.put("Book", Book);
        stats.put("Dalseong", Dalseong);
        stats.put("Goonwe", Goonwe);
        stats.put("Total", Total);
        
        return stats;
     }
	
	public Map<String, Integer> getHospitalCategoryCount() {
	    List<D_Hospital> hospitals = d_hr.findAll();
	    // 1. 모든 병원의 H_Categorie 값을 가져와 null이 아닌 경우만 처리
	    List<String> categories = hospitals.stream()
	        .map(D_Hospital::getH_Categorie)
	        .filter(Objects::nonNull)
	        .collect(Collectors.toList());
	    // 2. 다중 구분자를 기준으로 병원 개수를 집계
	    Map<String, Integer> categoryCount = new HashMap<>();
	    for (String categoryList : categories) {
	        // (소괄호) 안의 내용 제거
	        String cleanedCategory = categoryList.replaceAll("\\(.*?\\)", "");
			/*
			 \\( → 여는 괄호 ( 리터럴
			 \\) → 닫는 괄호 ) 리터럴
			 .*? → 가능한 짧게(비탐욕) 어떤 문자든 0개 이상
			 */	        // `/`, `,` 기준으로 분리
	        String[] categoryArray = cleanedCategory.split("[/,]+");
	        
	        for (String category : categoryArray) {
	            category = category.trim(); // 공백 제거
	            if (!category.isEmpty()) { // 빈 문자열 방지
	                categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
	            }
	        }
	    }
	    return categoryCount;
	}
}
