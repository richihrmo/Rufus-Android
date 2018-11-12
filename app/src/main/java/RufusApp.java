import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Richard Hrmo.
 */
public class RufusApp extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    FirebaseDatabase.getInstance().setPersistenceEnabled(true);
  }
}
