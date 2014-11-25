package com.telligent.model.daos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

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

	DecimalFormat df = new DecimalFormat("###.##");
	NumberFormat nf = NumberFormat.getInstance(Locale.US);
	
	@Override
	public ArrayList<SalarPlanningDTO> getSalaryPlanningDetails(String columnName,String order,String teamName,String teamId,String employeeId) {
		logger.info("in getSalaryPlanningDetails");
		StringBuffer query = new StringBuffer();
		query.append("select id,coworker_name,emp_id,supervisor,job_title,grade,type,rate,compa_ratio,minimum,midpoint,maximum,quartile,perf_grade,increment_percentage,new_rate,lumsum,updated_date ");
		query.append("from salary_planning sp,employee e,team t");
		query.append(" where sp.emp_id=e.employee_id and t.team_id=e.team_id and t.supervisor_employee_id =? and t.team_id = ?");
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
			ps.setString(2, teamId);
			rs = ps.executeQuery();
			while(rs.next()){
				SalarPlanningDTO dto = new SalarPlanningDTO();
				dto.setId(rs.getInt("id"));
				dto.setCoworker_name(rs.getString("coworker_name"));
				dto.setEmployeeId(rs.getString("emp_id"));
				dto.setSupervisor(rs.getString("supervisor"));
				dto.setJobTitle(rs.getString("job_title"));
				dto.setGrade(rs.getString("grade"));
				dto.setType(rs.getString("type"));
				try {dto.setRate(nf.format(rs.getDouble("rate")));} catch (Exception e) {}
				dto.setCompaRatio(rs.getString("compa_ratio"));
				dto.setMinimum(rs.getString("minimum"));
				dto.setMidpoint(rs.getString("midpoint"));
				dto.setMaximum(rs.getString("maximum"));
				dto.setQuartile(rs.getString("quartile"));
				dto.setPerfGrade(rs.getString("perf_grade"));
				dto.setIncrementPercentage(rs.getString("increment_percentage"));
				try {
					dto.setNewRate(nf.format(rs.getDouble("new_rate")));
				} catch (Exception e) {}
				dto.setLumsum(rs.getString("lumsum"));
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
    public boolean updateEmployeeDetails(SalarPlanningDTO salaryPlanDTO) {

                logger.info("in updateEmployeeDetails");
                StringBuffer updateQuery = new StringBuffer();
                boolean updated = false;
                String empId = salaryPlanDTO.getEmployeeId();
                String empGrade = salaryPlanDTO.getPerfGrade();
                String increment = salaryPlanDTO.getIncrementPercentage();
                float rate = Float.parseFloat(salaryPlanDTO.getRate().replaceAll(",", ""));
                float newRate = Float.parseFloat(salaryPlanDTO.getNewRate().replaceAll(",", ""));
                float  incrementPer = Float.parseFloat(increment);
                float maximum = Float.parseFloat(salaryPlanDTO.getMaximum());
                float lumsum = Float.parseFloat(salaryPlanDTO.getLumsum());


                float newSalary ;

                if (incrementPer > 0){
                    newSalary = rate*(1+incrementPer);
                    if (newSalary > maximum){
                        lumsum = newSalary - maximum;
                        newRate = maximum;
                    }else{
                    newRate = newSalary;
                    }
                }

                updateQuery.append("update salary_planning set perf_grade= ? ,increment_percentage= ?,new_rate= ?,lumsum=?,updated_date = sysdate() where emp_id = ? ");
                Connection conn = null;
                PreparedStatement ps = null;
                try{
                conn = this.getConnection();
                ps = conn.prepareStatement(updateQuery.toString());
                ps.setString(1, empGrade);
                ps.setFloat(2, incrementPer);
                ps.setFloat(3, newRate);
                ps.setFloat(4, lumsum);
                //ps.setDate(5, (java.sql.Date) new Date());
                ps.setString(5, empId);
                ps.executeUpdate();
               // conn.commit();
                updated = true;
                }catch(Exception ex){
                    updated = false;
                    ex.printStackTrace();
                    logger.error("Exception in updateEmployeeDetails :: "+ex);

                }finally{
                this.closeAll(conn, ps);
                }
        return updated;
       // throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    public ArrayList<BudgetSummaryDTO> budgetSummary(String supervisorId) {
    	// TODO Auto-generated method stub
    	
    	StringBuffer query = new StringBuffer();
		query.append("select type,sum(rate) current,sum(new_rate) new from salary_planning group by type ");
		ArrayList<BudgetSummaryDTO> list = new ArrayList<BudgetSummaryDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(query.toString());
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
				currentBudget = rs.getFloat("current")*2080;
				newBudget = rs.getFloat("new")*2080;
				}else{
					currentBudget = rs.getFloat("current");
					newBudget = rs.getFloat("new");
				}
				change = 1-(currentBudget/newBudget);
				totalCurrentBudget = totalCurrentBudget+currentBudget;
				totalnewBudget = totalnewBudget+newBudget;
				totalChange = totalChange+change;
				dto.setCurrentBudget(df.parse(String.valueOf(currentBudget))+"");
				dto.setNewBudget(df.parse(String.valueOf(newBudget))+"");
				dto.setChangeBudget(df.parse(String.valueOf(change))+" %");
				
				list.add(dto);
			}
			BudgetSummaryDTO dto1 = new BudgetSummaryDTO();
			dto1.setAnualBudgetType("Total");
			dto1.setCurrentBudget(df.parse(String.valueOf(totalCurrentBudget))+"");
			dto1.setNewBudget(df.parse(String.valueOf(totalnewBudget))+"");
			dto1.setChangeBudget(df.parse(String.valueOf(totalChange))+" %");
			list.add(dto1);
		}catch (Exception ex) {
			ex.printStackTrace();
			logger.info("Excpetion in annualBudgetSummary"+ex.getMessage());
		} finally {
			this.closeAll(conn, ps, rs);
		}
		return list;
    }

	@Override
	public ArrayList<RatingsAndIncreaseDTO> ratingsAndIncreaseSummary() {
		logger.info("in ratingsAndIncreaseSummary");
		StringBuffer query = new StringBuffer();
		query.append("select type,perf_grade,count(perf_grade) count from salary_planning group by type,perf_grade");
		ArrayList<RatingsAndIncreaseDTO> list = new ArrayList<RatingsAndIncreaseDTO>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(query.toString());
			rs = ps.executeQuery();
			RatingsAndIncreaseDTO dto = null;
			/*dto.setType("Performance Rating");
			dto.setHourlyA("A");
			dto.setHourlyB("B");
			dto.setHourlyC("C");
			dto.setOfficeA("A");
			dto.setOfficeB("B");
			dto.setOfficeC("C");
			list.add(dto);*/
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
			query.append("select type,perf_grade,(count/(select count(perf_grade) from salary_planning where b.type=type)) percentage from (select type,perf_grade,count(perf_grade) count from salary_planning b group by type,perf_grade) b");
			ps = conn.prepareStatement(query.toString());
			rs = ps.executeQuery();
			dto = new RatingsAndIncreaseDTO();
			dto.setType("% for Group");
			while(rs.next()){
				if(rs.getString("type").equalsIgnoreCase("hourly") && rs.getString("perf_grade")!=null){
					if(rs.getString("perf_grade").equalsIgnoreCase("A"))
						dto.setHourlyA(df.format(rs.getDouble(3)*100)+" %");
					else if(rs.getString("perf_grade").equalsIgnoreCase("B"))
						dto.setHourlyB(df.format(rs.getDouble(3)*100)+" %");
					else if(rs.getString("perf_grade").equalsIgnoreCase("C"))
						dto.setHourlyC(df.format(rs.getDouble(3)*100)+" %");
				}else if(rs.getString("type").equalsIgnoreCase("office") && rs.getString("perf_grade")!=null){
					if(rs.getString("perf_grade").equalsIgnoreCase("A"))
						dto.setOfficeA(df.format(rs.getDouble(3)*100)+" %");
					else if(rs.getString("perf_grade").equalsIgnoreCase("B"))
						dto.setOfficeB(df.format(rs.getDouble(3)*100)+" %");
					else if(rs.getString("perf_grade").equalsIgnoreCase("C"))
						dto.setOfficeC(df.format(rs.getDouble(3)*100)+" %");
				}
			}
			list.add(dto);
			
			
			rs.close();
			ps.close();
			query = new StringBuffer();
			rs = null;
			query.append("select type,perf_grade,avg(increment_percentage) count from salary_planning group by type,perf_grade");
			ps = conn.prepareStatement(query.toString());
			rs = ps.executeQuery();
			dto = new RatingsAndIncreaseDTO();
			dto.setType("Avg Increase");
			dto = setPercentages(rs,dto);
			list.add(dto);
			rs.close();
			ps.close();
			query = new StringBuffer();
			rs = null;
			query.append("select type,perf_grade,max(increment_percentage) count from salary_planning group by type,perf_grade");
			ps = conn.prepareStatement(query.toString());
			rs = ps.executeQuery();
			dto = new RatingsAndIncreaseDTO();
			dto.setType("High Increase");
			dto = setPercentages(rs,dto);
			list.add(dto);
			rs.close();
			ps.close();
			query = new StringBuffer();
			rs = null;
			query.append("select type,perf_grade,min(increment_percentage) count from salary_planning group by type,perf_grade");
			ps = conn.prepareStatement(query.toString());
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
	}
    private RatingsAndIncreaseDTO setPercentages(ResultSet rs, RatingsAndIncreaseDTO dto) throws SQLException{
    	while(rs.next()){
			if(rs.getString("type").equalsIgnoreCase("hourly") && rs.getString("perf_grade")!=null){
				if(rs.getString("perf_grade").equalsIgnoreCase("A"))
					dto.setHourlyA(df.format(rs.getDouble(3)*100)+" %");
				else if(rs.getString("perf_grade").equalsIgnoreCase("B"))
					dto.setHourlyB(df.format(rs.getDouble(3)*100)+" %");
				else if(rs.getString("perf_grade").equalsIgnoreCase("C"))
					dto.setHourlyC(df.format(rs.getDouble(3)*100)+" %");
			}else if(rs.getString("type").equalsIgnoreCase("office") && rs.getString("perf_grade")!=null){
				if(rs.getString("perf_grade").equalsIgnoreCase("A"))
					dto.setOfficeA(df.format(rs.getDouble(3)*100)+" %");
				else if(rs.getString("perf_grade").equalsIgnoreCase("B"))
					dto.setOfficeB(df.format(rs.getDouble(3)*100)+" %");
				else if(rs.getString("perf_grade").equalsIgnoreCase("C"))
					dto.setOfficeC(df.format(rs.getDouble(3)*100)+" %");
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
}
