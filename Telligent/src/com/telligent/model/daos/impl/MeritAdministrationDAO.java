package com.telligent.model.daos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import com.telligent.core.system.annotation.SpringBean;
import com.telligent.model.daos.IMeritAdministrationDAO;
import com.telligent.model.db.AbstractDBManager;
import com.telligent.model.dtos.BudgetSummaryDTO;
import com.telligent.model.dtos.RatingsAndIncreaseDTO;
import com.telligent.model.dtos.SalarPlanningDTO;
import com.telligent.model.dtos.SalaryPositionRangeDTO;

/**
 * @author spothu
 *
 */
@SpringBean
public class MeritAdministrationDAO extends AbstractDBManager implements IMeritAdministrationDAO{

	public final Logger logger = Logger.getLogger(MeritAdministrationDAO.class);

	DecimalFormat df = new DecimalFormat("###.00");
	NumberFormat nf = NumberFormat.getInstance(Locale.US);

	public MeritAdministrationDAO(){
		df.setMaximumFractionDigits(2);
	}

	@Override
	public ArrayList<SalarPlanningDTO> getSalaryPlanningDetails(String columnName,String order,String teamName,String teamId,String employeeId) {
		logger.info("in getSalaryPlanningDetails");
		StringBuffer query = new StringBuffer();
		query.append("select id,coworker_name,emp_id,supervisor,job_title,grade,type,rate,compa_ratio,minimum,midpoint,maximum,quartile,perf_grade,increment_percentage,new_rate,lumsum,DATE_FORMAT(updated_date,'%y/%m/%d') updated_date ");
		query.append("from salary_planning sp,employee e,team t");
		query.append(" where sp.emp_id=e.employee_id and t.team_id=e.team_id and t.supervisor_employee_id =? and t.team_id in("+teamId+")");
		if(columnName !=null && !columnName.equalsIgnoreCase("") && order!=null && !order.equalsIgnoreCase("")){
			if(columnName.equalsIgnoreCase("coworker_name"))
				query.append(" order by coworker_name "+order);
			else if(columnName.equalsIgnoreCase("employeeId"))
				query.append(" order by emp_id "+order);
		}
		ArrayList<SalarPlanningDTO> list = new ArrayList<SalarPlanningDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, employeeId);
			rs = ps.executeQuery();
			DecimalFormat df1 = new DecimalFormat("###.00");
			while(rs.next()){
				SalarPlanningDTO dto = new SalarPlanningDTO();
				dto.setId(rs.getInt("id"));
				dto.setCoworker_name(rs.getString("coworker_name"));
				dto.setEmployeeId(rs.getString("emp_id"));
				dto.setSupervisor(rs.getString("supervisor"));
				dto.setJobTitle(rs.getString("job_title"));
				dto.setGrade(rs.getString("grade"));
				dto.setType(rs.getString("type"));
				dto.setCompaRatio(rs.getString("compa_ratio"));
				try {dto.setRate(nf.format(Double.parseDouble(df.format(rs.getDouble("rate")))));} catch (Exception e) {}
				try {dto.setMinimum(nf.format(Double.parseDouble(df.format(rs.getDouble("minimum")))));} catch (Exception e) {}
				try {dto.setMidpoint(nf.format(Double.parseDouble(df.format(rs.getDouble("midpoint")))));} catch (Exception e) {}
				try {dto.setMaximum(nf.format(Double.parseDouble(df.format(rs.getDouble("maximum")))));} catch (Exception e) {}
				try {dto.setLumsum(nf.format(Double.parseDouble(df.format(rs.getDouble("lumsum")))));} catch (Exception e) {}
				dto.setQuartile(rs.getString("quartile"));
				dto.setPerfGrade(rs.getString("perf_grade"));
				if(Float.parseFloat(rs.getString("increment_percentage")) > 0)
					try {
						dto.setIncrementPercentage(df1.format(Float.parseFloat(rs.getString("increment_percentage"))) );
					} catch (Exception e1) {}
				else
					dto.setIncrementPercentage(rs.getString("increment_percentage"));
				try {
					dto.setNewRate(nf.format(Double.parseDouble(df.format(rs.getDouble("new_rate")))));
				} catch (Exception e) {}
				dto.setUpdatedDate(rs.getString("updated_date"));
				list.add(dto);
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			logger.info("Excpetion in getEmployeeTeam"+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return list;
	}

	@Override
	public boolean updateEmployeeDetails(JSONArray list) {

		logger.info("in updateEmployeeDetails");
		StringBuffer updateQuery = new StringBuffer();
		boolean updated = false;
		Connection conn = null;
		PreparedStatement ps = null;
		try{
			conn = this.getConnection();
			conn.setAutoCommit(false);
			for(int i=0;i<list.size();i++){
				String empId = list.getJSONObject(i).getString("employeeId");;
				String empGrade = list.getJSONObject(i).getString("perfGrade");
				String increment = list.getJSONObject(i).getString("incrementPercentage");
				String type = list.getJSONObject(i).getString("type");
				float rate = list.getJSONObject(i).getString("rate")!=null && !list.getJSONObject(i).getString("rate").equalsIgnoreCase("") ? Float.parseFloat(list.getJSONObject(i).getString("rate").replaceAll(",", "")):0;
				float newRate = list.getJSONObject(i).getString("newRate")!=null && !list.getJSONObject(i).getString("newRate").equalsIgnoreCase("") ? Float.parseFloat(list.getJSONObject(i).getString("newRate").replaceAll(",", "")):0;
				float  incrementPer = increment !=null && !increment.equalsIgnoreCase("") ? Float.parseFloat(increment):0;
				float maximum =list.getJSONObject(i).getString("maximum")!=null && !list.getJSONObject(i).getString("maximum").equalsIgnoreCase("") ? Float.parseFloat(list.getJSONObject(i).getString("maximum").replaceAll(",", "")):0; 
				float lumsum = list.getJSONObject(i).getString("lumsum")!=null && !list.getJSONObject(i).getString("lumsum").equalsIgnoreCase("") ? Float.parseFloat(list.getJSONObject(i).getString("lumsum").replaceAll(",", "")):0;
				float preUpdateNewRate ;
				if (incrementPer > 0){
					preUpdateNewRate = rate*(1+(incrementPer/100));
					if (preUpdateNewRate <= maximum){
						newRate = preUpdateNewRate;
						lumsum = 0;
					}else{
						if(type.equalsIgnoreCase("hourly") || type.equalsIgnoreCase("nonexempt")){
							newRate = maximum;	
							lumsum = ((preUpdateNewRate - maximum) * 2080);
						}else{
							newRate = maximum;	
							lumsum = preUpdateNewRate - maximum;
						}
					}
				}
				updateQuery = new StringBuffer();
				if(list.getJSONObject(i).getString("incrementPercentage")!=null && !list.getJSONObject(i).getString("incrementPercentage").equalsIgnoreCase("") 
						&& list.getJSONObject(i).getString("perfGrade")!=null && !list.getJSONObject(i).getString("perfGrade").equalsIgnoreCase(""))
					updateQuery.append("update salary_planning set perf_grade= ? ,increment_percentage= ? ,new_rate= ?,lumsum=?,updated_date = sysdate() where emp_id = ? ");
				else if(list.getJSONObject(i).getString("perfGrade")==null || list.getJSONObject(i).getString("perfGrade").equalsIgnoreCase(""))
					updateQuery.append("update salary_planning set increment_percentage= ? ,new_rate= ?,lumsum=?,updated_date = sysdate() where emp_id = ? ");
				else if(list.getJSONObject(i).getString("incrementPercentage")==null || list.getJSONObject(i).getString("incrementPercentage").equalsIgnoreCase(""))
					updateQuery.append("update salary_planning set perf_grade= ? ,new_rate= ?,lumsum=?,updated_date = sysdate() where emp_id = ? ");
				ps = conn.prepareStatement(updateQuery.toString());
				if(increment !=null && !increment.equalsIgnoreCase("") && empGrade !=null && !empGrade.equalsIgnoreCase("")){
					ps.setString(1, empGrade);
					ps.setFloat(2, incrementPer);
					ps.setFloat(3, newRate);
					ps.setFloat(4, lumsum);
					//ps.setDate(5, (java.sql.Date) new Date());
					ps.setString(5, empId);
				}else if(empGrade ==null || empGrade.equalsIgnoreCase("")){
					ps.setFloat(1, incrementPer);
					ps.setFloat(2, newRate);
					ps.setFloat(3, lumsum);
					//ps.setDate(5, (java.sql.Date) new Date());
					ps.setString(4, empId);
				}else if(increment ==null || increment.equalsIgnoreCase("")){
					ps.setString(1, empGrade);
					ps.setFloat(2, newRate);
					ps.setFloat(3, lumsum);
					//ps.setDate(5, (java.sql.Date) new Date());
					ps.setString(4, empId);
				}
				ps.addBatch();
				empId = null;empGrade=null;increment=null;
			}
			int i[] = ps.executeBatch();
			if(i.length == list.size()){
				conn.commit();
				updated = true;
			}
		}catch(Exception ex){
			updated = false;
			ex.printStackTrace();
			logger.error("Exception in updateEmployeeDetails :: "+ex);
			try {
				conn.rollback();
			} catch (SQLException e) {}
		}finally{
			this.closeAll(conn, ps);
		}
		return updated;
		// throw new UnsupportedOperationException("Not supported yet.");
	}


	@Override
	public ArrayList<BudgetSummaryDTO> budgetSummary(String supervisorId,String teamId,String teamName,String employeeId) {
		// TODO Auto-generated method stub

		StringBuffer query = new StringBuffer();
		//query.append("select type,sum(rate) current,sum(new_rate) new from salary_planning group by type ");
		query.append("select type,sum(rate) current,sum(new_rate) new ");
		query.append("from salary_planning sp,employee e,team t ");
		query.append(" where sp.emp_id=e.employee_id and t.team_id=e.team_id and t.supervisor_employee_id =? and t.team_id in ("+teamId+") group by type");

		ArrayList<BudgetSummaryDTO> list = new ArrayList<BudgetSummaryDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, employeeId);
			rs = ps.executeQuery();
			float totalCurrentBudget=0;
			float totalnewBudget=0;
			float totalChange=0;

			while(rs.next()){
				BudgetSummaryDTO dto = new BudgetSummaryDTO();
				float currentBudget;
				float newBudget;
				float change;
				dto.setAnualBudgetType(rs.getString("type"));
				if (dto.getAnualBudgetType() != null && dto.getAnualBudgetType().equalsIgnoreCase("Hourly")){
					currentBudget = Float.parseFloat(df.format(rs.getFloat("current")*2080));
					newBudget = Float.parseFloat(df.format(rs.getFloat("new")*2080));
				}else{
					currentBudget = Float.parseFloat(df.format(rs.getFloat("current")));
					newBudget = Float.parseFloat(df.format(rs.getFloat("new")));
				}
				change = Float.parseFloat(df.format(1-(currentBudget/newBudget)));
				totalCurrentBudget = totalCurrentBudget+currentBudget;
				totalnewBudget = totalnewBudget+newBudget;
				totalChange = totalChange+change;
				try {dto.setCurrentBudget(nf.format(Double.parseDouble(currentBudget+"")));} catch (Exception e) {}
				try {dto.setNewBudget(nf.format(Double.parseDouble(newBudget+"")));} catch (Exception e) {}
				try {dto.setChangeBudget(df.format(change*100));} catch (Exception e) {}
				list.add(dto);
			}
			BudgetSummaryDTO dto1 = new BudgetSummaryDTO();
			dto1.setAnualBudgetType("Total");
			try {dto1.setCurrentBudget(nf.format(Double.parseDouble(df.format(totalCurrentBudget))));} catch (Exception e) {}
			try {dto1.setNewBudget(nf.format(Double.parseDouble(df.format(totalnewBudget))));} catch (Exception e) {}
			try {dto1.setChangeBudget(df.format(totalChange*100.00));} catch (Exception e) {}
			list.add(dto1);
		}catch (Exception ex) {
			ex.printStackTrace();
			logger.info("Excpetion in annualBudgetSummary"+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return list;
	}

	// New Method
	@Override
	public ArrayList<RatingsAndIncreaseDTO> ratingsAndIncreaseSummary(String teamId,String teamName,String employeeId) {
		logger.info("in ratingsAndIncreaseSummary");
		StringBuffer query = new StringBuffer();
		//query.append("select type,perf_grade,count(perf_grade) count from salary_planning group by type,perf_grade");
		int hourlyEmployees = 0;
		int officeEmployees = 0;
		ArrayList<RatingsAndIncreaseDTO> list = new ArrayList<RatingsAndIncreaseDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			// Start End query to get Count
			query.append("select type,perf_grade,count(perf_grade) count ");
			query.append("from salary_planning sp,employee e,team t ");
			query.append(" where sp.emp_id=e.employee_id and t.team_id=e.team_id and t.supervisor_employee_id =? and t.team_id in ("+teamId+") group by type,perf_grade");
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, employeeId);
			rs = ps.executeQuery();
			RatingsAndIncreaseDTO dto = null;
			dto = new RatingsAndIncreaseDTO();
			dto.setType("Count");
			HashMap<String, Integer> countMap = new HashMap<String, Integer>();
			while(rs.next()){
				if(rs.getString("type").equalsIgnoreCase("hourly") && rs.getString("perf_grade")!=null){
					if(rs.getString("perf_grade").equalsIgnoreCase("A")){
						dto.setHourlyA(rs.getString("count"));
						countMap.put("hourlyA", rs.getInt("count"));
					}else if(rs.getString("perf_grade").equalsIgnoreCase("B")){
						dto.setHourlyB(rs.getString("count"));
						countMap.put("hourlyB", rs.getInt("count"));
					}else if(rs.getString("perf_grade").equalsIgnoreCase("C")){
						dto.setHourlyC(rs.getString("count"));
						countMap.put("hourlyC", rs.getInt("count"));
					}
				}else if(rs.getString("type").equalsIgnoreCase("office") && rs.getString("perf_grade")!=null){
					if(rs.getString("perf_grade").equalsIgnoreCase("A")){
						dto.setOfficeA(rs.getString("count"));
						countMap.put("officeA", rs.getInt("count"));
					}else if(rs.getString("perf_grade").equalsIgnoreCase("B")){
						dto.setOfficeB(rs.getString("count"));
						countMap.put("officeB", rs.getInt("count"));
					}else if(rs.getString("perf_grade").equalsIgnoreCase("C")){
						dto.setOfficeC(rs.getString("count"));
						countMap.put("officeC", rs.getInt("count"));
					}
				}
			}
			list.add(dto);
			rs.close();
			ps.close();
			query = new StringBuffer();
			// End query to get Count
			// Start query to get total hourly / Office employees
			query.append("select type,count(type) count ");
			query.append("from salary_planning sp,employee e,team t ");
			query.append(" where sp.emp_id=e.employee_id and t.team_id=e.team_id and t.supervisor_employee_id =? and t.team_id in ("+teamId+") group by type");
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, employeeId);
			rs = ps.executeQuery();
			while(rs.next()){
				if(rs.getString("type")!=null && rs.getString("type").equalsIgnoreCase("hourly")){
					hourlyEmployees = rs.getInt("count");
				}else if(rs.getString("type")!=null && rs.getString("type").equalsIgnoreCase("office")){
					officeEmployees = rs.getInt("count");
				}
			}
			rs.close();
			ps.close();
			// End query to get total hourly / Office employees
			query = new StringBuffer();
			rs = null;
			// Start - % for Group
			//query.append("select type,perf_grade,(count/(select count(perf_grade) from salary_planning where b.type=type)) percentage from (select type,perf_grade,count(perf_grade) count from salary_planning b group by type,perf_grade) b");
			//query.append("select type,perf_grade,(count/(select count(perf_grade) from salary_planning where b.type=type)) percentage from (select type,perf_grade,count(perf_grade) count from salary_planning b,employee e,team t  where b.emp_id=e.employee_id and t.team_id=e.team_id and t.supervisor_employee_id =? and t.team_id in ("+teamId+") group by type,perf_grade) b");
			query.append("select type,perf_grade,count(perf_grade) count ");
			query.append("from salary_planning sp,employee e,team t ");
			query.append(" where sp.emp_id=e.employee_id and t.team_id=e.team_id and t.supervisor_employee_id =? and t.team_id in ("+teamId+") group by type,perf_grade");
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, employeeId);
			rs = ps.executeQuery();
			dto = new RatingsAndIncreaseDTO();
			dto.setType("% for Group");
			while(rs.next()){
				if(rs.getString("type").equalsIgnoreCase("hourly") && rs.getString("perf_grade")!=null){
					if(rs.getString("perf_grade").equalsIgnoreCase("A"))
						dto.setHourlyA(df.format((rs.getDouble(3)/hourlyEmployees)*100));
					else if(rs.getString("perf_grade").equalsIgnoreCase("B"))
						dto.setHourlyB(df.format((rs.getDouble(3)/hourlyEmployees)*100));
					else if(rs.getString("perf_grade").equalsIgnoreCase("C"))
						dto.setHourlyC(df.format((rs.getDouble(3)/hourlyEmployees)*100));
				}else if(rs.getString("type").equalsIgnoreCase("office") && rs.getString("perf_grade")!=null){
					if(rs.getString("perf_grade").equalsIgnoreCase("A"))
						dto.setOfficeA(df.format((rs.getDouble(3)/officeEmployees)*100));
					else if(rs.getString("perf_grade").equalsIgnoreCase("B"))
						dto.setOfficeB(df.format((rs.getDouble(3)/officeEmployees)*100));
					else if(rs.getString("perf_grade").equalsIgnoreCase("C"))
						dto.setOfficeC(df.format((rs.getDouble(3)/officeEmployees)*100));
				}
			}
			list.add(dto);

			// End - % for Group
			rs.close();
			ps.close();
			query = new StringBuffer();
			rs = null;
			//query.append("select type,perf_grade,avg(increment_percentage) count from salary_planning group by type,perf_grade");
			query.append("select type,perf_grade,sum(increment_percentage) count ");
			query.append("from salary_planning sp,employee e,team t ");
			query.append(" where sp.emp_id=e.employee_id and t.team_id=e.team_id and t.supervisor_employee_id =? and t.team_id in ("+teamId+") group by type,perf_grade");
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, employeeId);
			rs = ps.executeQuery();
			dto = new RatingsAndIncreaseDTO();
			dto.setType("Avg Increase");
			dto = setAvg(rs,dto,countMap);
			list.add(dto);
			rs.close();
			ps.close();
			query = new StringBuffer();
			rs = null;
			//query.append("select type,perf_grade,max(increment_percentage) count from salary_planning group by type,perf_grade");
			query.append("select type,perf_grade,max(increment_percentage) count ");
			query.append("from salary_planning sp,employee e,team t ");
			query.append(" where sp.emp_id=e.employee_id and t.team_id=e.team_id and t.supervisor_employee_id =? and t.team_id in ("+teamId+") group by type,perf_grade");
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, employeeId);
			rs = ps.executeQuery();
			dto = new RatingsAndIncreaseDTO();
			dto.setType("High Increase");
			dto = setHighLowIncrease(rs,dto);
			list.add(dto);
			rs.close();
			ps.close();
			query = new StringBuffer();
			rs = null;
			//query.append("select type,perf_grade,min(increment_percentage) count from salary_planning group by type,perf_grade");
			query.append("select type,perf_grade,min(increment_percentage) count ");
			query.append("from salary_planning sp,employee e,team t ");
			query.append(" where sp.emp_id=e.employee_id and t.team_id=e.team_id and t.supervisor_employee_id =? and t.team_id in ("+teamId+") group by type,perf_grade");
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, employeeId);
			rs = ps.executeQuery();
			dto = new RatingsAndIncreaseDTO();
			dto.setType("Low Increase");
			dto = setHighLowIncrease(rs,dto);
			list.add(dto);
		}catch (Exception ex) {
			ex.printStackTrace();
			logger.info("Excpetion in ratingsAndIncreaseSummary"+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return list;
	}
	// Old method
	/*@Override
	public ArrayList<RatingsAndIncreaseDTO> ratingsAndIncreaseSummary(String teamId,String teamName,String employeeId) {
		logger.info("in ratingsAndIncreaseSummary");
		StringBuffer query = new StringBuffer();
		//query.append("select type,perf_grade,count(perf_grade) count from salary_planning group by type,perf_grade");
		query.append("select type,perf_grade,count(perf_grade) count ");
		query.append("from salary_planning sp,employee e,team t ");
		query.append(" where sp.emp_id=e.employee_id and t.team_id=e.team_id and t.supervisor_employee_id =? and t.team_id in ("+teamId+") group by type,perf_grade");

		ArrayList<RatingsAndIncreaseDTO> list = new ArrayList<RatingsAndIncreaseDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, employeeId);
			rs = ps.executeQuery();
			RatingsAndIncreaseDTO dto = null;
			dto.setType("Performance Rating");
			dto.setHourlyA("A");
			dto.setHourlyB("B");
			dto.setHourlyC("C");
			dto.setOfficeA("A");
			dto.setOfficeB("B");
			dto.setOfficeC("C");
			list.add(dto);
			dto = new RatingsAndIncreaseDTO();
			dto.setType("Count");
			while(rs.next()){
				if(rs.getString("type").equalsIgnoreCase("hourly") && rs.getString("perf_grade")!=null){
					if(rs.getString("perf_grade").equalsIgnoreCase("A"))
						dto.setHourlyA(rs.getString("count"));
					else if(rs.getString("perf_grade").equalsIgnoreCase("B"))
						dto.setHourlyB(rs.getString("count"));
					else if(rs.getString("perf_grade").equalsIgnoreCase("C"))
						dto.setHourlyC(rs.getString("count"));
				}else if(rs.getString("type").equalsIgnoreCase("office") && rs.getString("perf_grade")!=null){
					if(rs.getString("perf_grade").equalsIgnoreCase("A"))
						dto.setOfficeA(rs.getString("count"));
					else if(rs.getString("perf_grade").equalsIgnoreCase("B"))
						dto.setOfficeB(rs.getString("count"));
					else if(rs.getString("perf_grade").equalsIgnoreCase("C"))
						dto.setOfficeC(rs.getString("count"));
				}
			}
			list.add(dto);
			rs.close();
			ps.close();
			query = new StringBuffer();
			rs = null;
			//query.append("select type,perf_grade,(count/(select count(perf_grade) from salary_planning where b.type=type)) percentage from (select type,perf_grade,count(perf_grade) count from salary_planning b group by type,perf_grade) b");
			query.append("select type,perf_grade,(count/(select count(perf_grade) from salary_planning where b.type=type)) percentage from (select type,perf_grade,count(perf_grade) count from salary_planning b,employee e,team t  where b.emp_id=e.employee_id and t.team_id=e.team_id and t.supervisor_employee_id =? and t.team_id in ("+teamId+") group by type,perf_grade) b");
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, employeeId);
			rs = ps.executeQuery();
			dto = new RatingsAndIncreaseDTO();
			dto.setType("% for Group");
			while(rs.next()){
				if(rs.getString("type").equalsIgnoreCase("hourly") && rs.getString("perf_grade")!=null){
					if(rs.getString("perf_grade").equalsIgnoreCase("A"))
						dto.setHourlyA(df.format(rs.getDouble(3)*100));
					else if(rs.getString("perf_grade").equalsIgnoreCase("B"))
						dto.setHourlyB(df.format(rs.getDouble(3)*100));
					else if(rs.getString("perf_grade").equalsIgnoreCase("C"))
						dto.setHourlyC(df.format(rs.getDouble(3)*100));
				}else if(rs.getString("type").equalsIgnoreCase("office") && rs.getString("perf_grade")!=null){
					if(rs.getString("perf_grade").equalsIgnoreCase("A"))
						dto.setOfficeA(df.format(rs.getDouble(3)*100));
					else if(rs.getString("perf_grade").equalsIgnoreCase("B"))
						dto.setOfficeB(df.format(rs.getDouble(3)*100));
					else if(rs.getString("perf_grade").equalsIgnoreCase("C"))
						dto.setOfficeC(df.format(rs.getDouble(3)*100));
				}
			}
			list.add(dto);


			rs.close();
			ps.close();
			query = new StringBuffer();
			rs = null;
			//query.append("select type,perf_grade,avg(increment_percentage) count from salary_planning group by type,perf_grade");
			query.append("select type,perf_grade,avg(increment_percentage) count ");
			query.append("from salary_planning sp,employee e,team t ");
			query.append(" where sp.emp_id=e.employee_id and t.team_id=e.team_id and t.supervisor_employee_id =? and t.team_id in ("+teamId+") group by type,perf_grade");
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, employeeId);
			rs = ps.executeQuery();
			dto = new RatingsAndIncreaseDTO();
			dto.setType("Avg Increase");
			dto = setPercentages(rs,dto);
			list.add(dto);
			rs.close();
			ps.close();
			query = new StringBuffer();
			rs = null;
			//query.append("select type,perf_grade,max(increment_percentage) count from salary_planning group by type,perf_grade");
			query.append("select type,perf_grade,max(increment_percentage) count ");
			query.append("from salary_planning sp,employee e,team t ");
			query.append(" where sp.emp_id=e.employee_id and t.team_id=e.team_id and t.supervisor_employee_id =? and t.team_id in ("+teamId+") group by type,perf_grade");
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, employeeId);
			rs = ps.executeQuery();
			dto = new RatingsAndIncreaseDTO();
			dto.setType("High Increase");
			dto = setPercentages(rs,dto);
			list.add(dto);
			rs.close();
			ps.close();
			query = new StringBuffer();
			rs = null;
			//query.append("select type,perf_grade,min(increment_percentage) count from salary_planning group by type,perf_grade");
			query.append("select type,perf_grade,min(increment_percentage) count ");
			query.append("from salary_planning sp,employee e,team t ");
			query.append(" where sp.emp_id=e.employee_id and t.team_id=e.team_id and t.supervisor_employee_id =? and t.team_id in ("+teamId+") group by type,perf_grade");
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, employeeId);
			rs = ps.executeQuery();
			dto = new RatingsAndIncreaseDTO();
			dto.setType("Low Increase");
			dto = setPercentages(rs,dto);
			list.add(dto);
		}catch (Exception ex) {
			ex.printStackTrace();
			logger.info("Excpetion in ratingsAndIncreaseSummary"+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return list;
	}*/
	private RatingsAndIncreaseDTO setAvg(ResultSet rs, RatingsAndIncreaseDTO dto,HashMap<String, Integer> countMap) throws SQLException{
		while(rs.next()){//{hourlyA=1, hourlyc=1, officeA=3, officeC=3, officeB=1}
			if(rs.getString("type").equalsIgnoreCase("hourly") && rs.getString("perf_grade")!=null){
				if(rs.getString("perf_grade").equalsIgnoreCase("A")){
					dto.setHourlyA(df.format(rs.getDouble(3)/countMap.get("hourlyA")));
				}else if(rs.getString("perf_grade").equalsIgnoreCase("B")){
					dto.setHourlyB(df.format(rs.getDouble(3)/countMap.get("hourlyB")));
				}else if(rs.getString("perf_grade").equalsIgnoreCase("C")){
					dto.setHourlyC(df.format(rs.getDouble(3)/countMap.get("hourlyC")));
				}
			}else if(rs.getString("type").equalsIgnoreCase("office") && rs.getString("perf_grade")!=null){
				if(rs.getString("perf_grade").equalsIgnoreCase("A")){
					dto.setOfficeA(df.format(rs.getDouble(3)/countMap.get("officeA")));
				}else if(rs.getString("perf_grade").equalsIgnoreCase("B")){
					dto.setOfficeB(df.format(rs.getDouble(3)/countMap.get("officeB")));
				}else if(rs.getString("perf_grade").equalsIgnoreCase("C")){
					dto.setOfficeC(df.format(rs.getDouble(3)/countMap.get("officeC")));
				}
			}
		}
		return dto;
	}

	private RatingsAndIncreaseDTO setHighLowIncrease(ResultSet rs, RatingsAndIncreaseDTO dto) throws SQLException{
		while(rs.next()){
			if(rs.getString("type").equalsIgnoreCase("hourly") && rs.getString("perf_grade")!=null){
				if(rs.getString("perf_grade").equalsIgnoreCase("A"))
					dto.setHourlyA(df.format(rs.getDouble(3)));
				else if(rs.getString("perf_grade").equalsIgnoreCase("B"))
					dto.setHourlyB(df.format(rs.getDouble(3)));
				else if(rs.getString("perf_grade").equalsIgnoreCase("C"))
					dto.setHourlyC(df.format(rs.getDouble(3)));
			}else if(rs.getString("type").equalsIgnoreCase("office") && rs.getString("perf_grade")!=null){
				if(rs.getString("perf_grade").equalsIgnoreCase("A"))
					dto.setOfficeA(df.format(rs.getDouble(3)));
				else if(rs.getString("perf_grade").equalsIgnoreCase("B"))
					dto.setOfficeB(df.format(rs.getDouble(3)));
				else if(rs.getString("perf_grade").equalsIgnoreCase("C"))
					dto.setOfficeC(df.format(rs.getDouble(3)));
			}
		}
		return dto;
	}

	@Override
	public ArrayList<SalaryPositionRangeDTO> salaryPositionRangeDetails() {
		logger.info("in salaryPositionRangeDetails");
		StringBuffer query = new StringBuffer();
		query.append("select id,overallperformanceRating,aggregate_expected,first_quartile,second_quartile,third_quartile,fourth_quartile from position_sal_range order by id");
		ArrayList<SalaryPositionRangeDTO> list = new ArrayList<SalaryPositionRangeDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(query.toString());
			rs = ps.executeQuery();
			while(rs.next()){
				SalaryPositionRangeDTO dto = new SalaryPositionRangeDTO();
				dto.setOverallPerformanceRating(rs.getString("overallperformanceRating"));
				dto.setAggregateExpected(rs.getString("aggregate_expected"));
				dto.setFirstQuartile(rs.getString("first_quartile"));
				dto.setSecondQuartile(rs.getString("second_quartile"));
				dto.setThirdQuartile(rs.getString("third_quartile"));
				dto.setFourthQuartile(rs.getString("fourth_quartile"));
				list.add(dto);				
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			logger.info("Excpetion in salaryPositionRangeDetails"+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return list;
	}

	@Override
	public HashMap<String, Integer> getSalaryPlanningGridView(String empId) {
		logger.info("in getSalaryPlanningGridView");
		StringBuffer query = new StringBuffer();
		query.append("select employeeIdwidth,coworker_name,type,rate,perfGrade,incrementPercentage,newRate,lumsum,jobTitle,updatedDate,grade,compaRatio,minimum,midpoint,maximum,quartile from salary_planning_gridView where employeeId=?");
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, empId);
			rs = ps.executeQuery();
			if(rs.next()){
				map.put("employeeId", rs.getInt("employeeIdwidth"));
				map.put("coworker_name", rs.getInt("coworker_name"));
				map.put("type", rs.getInt("type"));
				map.put("rate", rs.getInt("rate"));
				map.put("perfGrade", rs.getInt("perfGrade"));
				map.put("incrementPercentage", rs.getInt("incrementPercentage"));
				map.put("newRate", rs.getInt("newRate"));
				map.put("lumsum", rs.getInt("lumsum"));
				map.put("jobTitle", rs.getInt("jobTitle"));
				map.put("updatedDate", rs.getInt("updatedDate"));
				map.put("grade", rs.getInt("grade"));
				map.put("compaRatio", rs.getInt("compaRatio"));
				map.put("minimum", rs.getInt("minimum"));
				map.put("midpoint", rs.getInt("midpoint"));
				map.put("maximum", rs.getInt("maximum"));
				map.put("quartile", rs.getInt("quartile"));
			}else{ // Set default width
				map.put("employeeId", 50);
				map.put("coworker_name", 50);
				map.put("type", 50);
				map.put("rate", 50);
				map.put("perfGrade", 50);
				map.put("incrementPercentage", 50);
				map.put("newRate", 50);
				map.put("lumsum", 50);
				map.put("jobTitle", 50);
				map.put("updatedDate", 50);
				map.put("grade", 50);
				map.put("compaRatio", 50);
				map.put("minimum", 50);
				map.put("midpoint", 50);
				map.put("maximum", 50);
				map.put("quartile", 50);
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			logger.info("Excpetion in getSalaryPlanningGridView"+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return map;
	}

	public int setDefaultWidth(int width){
		//if(width != null)

		return width;
	}

	@Override
	public void updateSalaryPlanningColumnWidth(String field, String width,String empId) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer(); 
		try {
			if(!field.equalsIgnoreCase("id")){
				if(field.equalsIgnoreCase("employeeId"))
					field = "employeeIdwidth";
				conn = this.getConnection();
				boolean flag = checkSalaryPlanningColumnWidthExists(conn, ps, rs, empId);
				if(flag){ // update
					query.append("update salary_planning_gridView set ");
					query.append(" "+field+" = '"+width+"'");
					query.append(" where employeeId = ?");
					ps = conn.prepareStatement(query.toString());
					ps.setString(1, empId);
				}else{ // insert
					query.append("Insert into salary_planning_gridView (employeeId,"+field+" ) values (?,?)");
					ps = conn.prepareStatement(query.toString());
					ps.setString(1, empId);
					ps.setString(2, width);
				}
				int i =ps.executeUpdate();
				logger.info("Result in updateSalaryPlanningColumnWidth = field "+field+" width = "+width+" employee Id = "+empId+" i =="+i);
			}
		}catch (Exception ex) {
			logger.info("Excpetion in updateSalaryPlanningColumnWidth"+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
	}

	private boolean checkSalaryPlanningColumnWidthExists(Connection conn,PreparedStatement ps,ResultSet rs,String empId){
		try{
			ps = conn.prepareStatement("select employeeId from salary_planning_gridView where employeeId=?");
			ps.setString(1, empId);
			rs = ps.executeQuery();
			if(rs.next())
				return true;
			else
				return false;
		}catch(Exception e){

		}
		return false;
	}
}
