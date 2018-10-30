package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import org.json.JSONObject;

/**
 * Servlet implementation class FirstServlet
 */
@WebServlet("/FirstServlet")
public class FirstServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FirstServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/plain");
		request.setCharacterEncoding("UTF-8");
		String countryID = request.getParameter("countryID");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		PrintWriter out = response.getWriter();
		
		try {
			out.println(parser(countryID,startDate,endDate));			
		} catch (Exception e) {
			e.printStackTrace();
		}
//		doGet(request, response);
	}

	protected Object parser(String Currency,String startDate,String endDate) throws Exception {		
		final String URL = "https://www.esunbank.com.tw/bank/Layouts/esunbank/Deposit/DpService.aspx/GetLineChartJson";
		String SetCurrency = Currency;
		String SetCurrencytype = "1";
		String SetRangetype = "3";
		String SetStartdate = startDate;
		String SetEnddate = endDate;
//		String SetCurrencyTitle = "美元(USD)";		
		HttpURLConnection connection = null;

		try {
			//建立連結
			URL url = new URL(URL);
			connection = (HttpURLConnection) url.openConnection();
			
			//設定連接屬性(HTTP)
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setInstanceFollowRedirects(true);
			
			//設定HTTP Header
			connection.setRequestProperty("Content-Type", "application/json");//設定請求格式json
			connection.setRequestProperty("Referer","https://www.esunbank.com.tw/bank/personal/deposit/rate/forex/exchange-rate-chart");
			connection.connect();
			
			//添加請求內容{"Currency":"USD","Currencytype":"1","Rangetype":"3","Startdate":"2017-10-29","Enddate":"2018-10-29","CurrencyTitle":"美元(USD)"}
			JSONObject data = new JSONObject();
			data.put("Currency", SetCurrency);
			data.put("Currencytype", SetCurrencytype);
			data.put("Rangetype", SetRangetype);
			data.put("Startdate", SetStartdate);
			data.put("Enddate", SetEnddate);
//			data.put("CurrencyTitle", SetCurrencyTitle);
			
			//建構json {"data":{"Currency":"USD","Currencytype":"1","Rangetype":"3","Startdate":"2017-10-25","Enddate":"2018-10-25","CurrencyTitle":"美元(USD)"}}			
			JSONObject dataobj = new JSONObject();
			dataobj.put("data", data);
			OutputStream out = connection.getOutputStream();
			out.write(dataobj.toString().getBytes());
			out.flush();
			out.close();
			
			//讀取回應
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String lines;
			StringBuffer sb = new StringBuffer("");
			while ((lines = reader.readLine()) != null) {
				lines = new String(lines.getBytes(), "utf-8");
				sb.append(lines);
			}			
			reader.close();
			
			//結束連結
			connection.disconnect();

			JSONObject jsonSbObj = new JSONObject(sb.toString());
			JSONObject rateSbObj = new JSONObject(jsonSbObj.getString("d").toString());
//			System.out.println(rateSbObj.getString("Rates"));
			
			//回傳需要的json array [{"BuyRate": "","SellRate": "", "Time":""}]
			return rateSbObj.getString("Rates").toString();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return 0;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}

	}

}
