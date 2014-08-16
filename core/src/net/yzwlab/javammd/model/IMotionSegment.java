package net.yzwlab.javammd.model;

/**
 * モーションの区分を示すインタフェースです。
 * 모션 세그먼트를 나타내는 인터페이스입니다
 */
public interface IMotionSegment {

	/**
	 * 開始時間を取得します。
	 * 시작 시간을 가져옵니다
	 * 
	 * @return 開始時間。
	 */
	public int getStart();

	/**
	 * 停止時間を取得します。
	 * 정지 시간을 가져옵니다
	 * 
	 * @return 停止時間。
	 */
	public int getStop();

	/**
	 * フレーム番号を取得します。
	 * 프레임 번호를 가져옵니다
	 * 
	 * @param frameRate
	 *            フレームレート。	프레임 속도
	 * @param currentTime
	 *            現在時刻。ミリ秒で表現する。	현재 시간. 밀리 세컨드로 표현한다.
	 * @return フレーム番号。프레임 번호
	 */
	public float getFrame(float frameRate, long currentTime);

}
