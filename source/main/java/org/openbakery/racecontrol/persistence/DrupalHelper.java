package org.openbakery.racecontrol.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.openbakery.racecontrol.persistence.bean.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrupalHelper implements ProfileHelper {

	private Logger log = LoggerFactory.getLogger(DrupalHelper.class);

	private static Pattern lfsWorldNamePattern = Pattern.compile("profile_lfsworldusername\";s:\\d*:\"([^\"]*)");

	private Persistence persistence;

	public DrupalHelper(Persistence persistence) {
		this.persistence = persistence;
	}

	public List<Profile> getSignedUpDrivers(int signupId) throws PersistenceException {
		TreeSet<String> lfsworldNames = new TreeSet<String>();
		log.debug("execute profileValue query");

		StringBuilder query = new StringBuilder();
		query.append("SELECT first.uid AS uid, first.value AS firstname, last.value AS lastname, lfsworld.value as lfsworldName ");
		query.append("FROM oelfs_profile_values as first ");
		query.append("INNER JOIN oelfs_profile_values AS last ON first.uid = last.uid AND last.fid=6 ");
		query.append("INNER JOIN oelfs_profile_values AS lfsworld ON first.uid = lfsworld.uid AND lfsworld.fid=1 ");
		query.append("INNER JOIN oelfs_signup_log AS signup ON first.uid = signup.uid ");
		query.append("WHERE first.fid=7 ");
		query.append("AND signup.nid = ");
		query.append(signupId);

		List<Object[]> result = (List<Object[]>) persistence.queryNative(query.toString(), "profile");
		List<Profile> profile = new ArrayList<Profile>(result.size());
		for (Object[] data : result) {
			profile.add(new Profile(((Integer) data[0]).intValue(), (String) data[1], (String) data[2], (String) data[3]));
		}
		return profile;

	}
	// public Set<String> getSignedUpDrivers(int signupId) throws
	// PersistenceException {
	//
	// TreeSet<String> lfsworldNames = new TreeSet<String>();
	// log.debug("execute profileValue query");
	// List profileValue = persistence.queryNative(
	// "SELECT value FROM oelfs_profile_values v, oelfs_signup_log as s WHERE v.uid = s.uid AND v.fid=1 AND s.nid = "
	// + signupId,
	// "profileValue");
	// for (Object object : profileValue) {
	// if (log.isDebugEnabled()) {
	// log.debug("lfsworld name: " + object + "-");
	// }
	// if (object != null) {
	// lfsworldNames.add(object.toString().toLowerCase().trim());
	// }
	// }
	//
	// profileValue = persistence
	// .queryNative(
	// "select data as value from oelfs_users as u,  oelfs_signup_log as s where u.uid = s.uid AND data like '%profile_lfsworldusername%' AND s.nid = "
	// + signupId, "profileValue");
	// for (Object object : profileValue) {
	// if (log.isDebugEnabled()) {
	// log.debug("lfsworld name 2: " + object.toString() + "-");
	// }
	// Matcher matcher = lfsWorldNamePattern.matcher(object.toString());
	// if (matcher.find()) {
	// String lfsWorldName = matcher.group(1);
	// log.debug("lfsworld name match found {} ", lfsWorldName);
	//
	// lfsworldNames.add(lfsWorldName.toLowerCase().trim());
	// }
	// }
	//
	// if (log.isDebugEnabled()) {
	// for (String name : lfsworldNames) {
	// log.debug("signed up lfsworld name: {}", name);
	// }
	// }
	//
	// return lfsworldNames;
	// }

}
